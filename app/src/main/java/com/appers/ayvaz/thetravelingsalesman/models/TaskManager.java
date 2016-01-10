package com.appers.ayvaz.thetravelingsalesman.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.CalendarContract;

import com.appers.ayvaz.thetravelingsalesman.database.DatabaseHelper;
import com.appers.ayvaz.thetravelingsalesman.database.DbSchema;
import com.appers.ayvaz.thetravelingsalesman.database.DbSchema.TaskTable.Cols;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by D on 030 12/30.
 */
public class TaskManager {
    private static TaskManager mTaskManager;

    private static String[] projections = {
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.HAS_ALARM,
            CalendarContract.Events.HAS_ATTENDEE_DATA,
            CalendarContract.Events.DESCRIPTION    };



    private Context mContext;
    private SQLiteDatabase mDatabase;
    private List<TaskII> messageList;

    private TaskManager(Context context) {
        mContext = context;
        mDatabase = new DatabaseHelper(mContext)
                .getWritableDatabase();
    }

    ;

    public static TaskManager get(Context context) {
        if (mTaskManager == null) {
            mTaskManager = new TaskManager(context);
        }
        return mTaskManager;
    }


    public void addTask(TaskII item) {
        ContentValues values = getContentValues(item);
        mDatabase.insert(DbSchema.TaskTable.NAME, null, values);
    }

    private ContentValues getContentValues(TaskII item) {
        ContentValues values = new ContentValues();
        values.put(Cols.CLIENT_ID, item.getClient().getId().toString());
        values.put(Cols.EVENT_ID, item.getId());
        return values;

    }

    public List<TaskII> query(Client client) {
        List<TaskII> taskIIList = new ArrayList<>();

        Cursor cursor = mDatabase.query(
                DbSchema.TaskTable.NAME,
                null,
                Cols.CLIENT_ID + " = ?",
                new String[]{client.getId().toString()},
                null,
                null,
                null
        );

        if (cursor == null || cursor.getCount() == 0) {
            return taskIIList;
        }

        cursor.moveToFirst();

        do {
            long eventId = cursor.getLong(cursor.getColumnIndex(Cols.EVENT_ID));
            Cursor eventCursor = queryEvent(eventId);
            if (eventCursor == null || eventCursor.getCount() == 0) {
                continue;
            }
            eventCursor.moveToFirst();
            TaskII taskII = TaskII.fromCursor(eventCursor);
            taskII.setClient(client);
            taskIIList.add(taskII);
            eventCursor.close();
        } while (cursor.moveToNext());
        cursor.close();
        return taskIIList;
    }

    private Cursor queryEvent(long eventId) {

        String selection = CalendarContract.Events._ID + " = ?";
        String[] selectionARgs = new String[]{Long.toString(eventId)};
        return mContext.getContentResolver().query(CalendarContract.Events.CONTENT_URI, projections,
                selection, selectionARgs, null);

    }


}
