package com.appers.ayvaz.thetravelingsalesman.models;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import com.appers.ayvaz.thetravelingsalesman.database.DatabaseHelper;
import com.appers.ayvaz.thetravelingsalesman.database.DbSchema;
import com.appers.ayvaz.thetravelingsalesman.database.DbSchema.TaskTable.Cols;
import com.appers.ayvaz.thetravelingsalesman.utils.DateTimeHelper;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
            CalendarContract.Events.DESCRIPTION};

    private final String DEBUG_TAG = "TaskManager debug:";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private LocalDate selectedDate;

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


    public void addTask(Task item) {
        ContentValues values = getContentValues(item);
        mDatabase.insert(DbSchema.TaskTable.NAME, null, values);
    }

    private ContentValues getContentValues(Task item) {
        ContentValues values = new ContentValues();
        values.put(Cols.CLIENT_ID, item.getClient().getId().toString());
        values.put(Cols.EVENT_ID, item.getId());
        return values;

    }

    public List<Task> query(UUID clientId) {
        List<Task> taskList = new ArrayList<>();
        List<Long> deleteList = new ArrayList<>();

        // first, find all eventId that is related to the client
        Cursor cursor = mDatabase.query(
                DbSchema.TaskTable.NAME,
                null,
                Cols.CLIENT_ID + " = ?",
                new String[]{clientId.toString()},
                null,
                null,
                null
        );

        if (cursor == null || cursor.getCount() == 0) {
            Log.i("...........", "empty list");
            return taskList;

        }
        // second, for each event that belongs to this client, find all instances that haven't occurred
        while (cursor.moveToNext()) {

            long eventId = cursor.getLong(cursor.getColumnIndex(Cols.EVENT_ID));

            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            Calendar end = Calendar.getInstance();
            end.set(2050, 11, 30, 23, 59);

            List<Task> result = queryInstance(now, end, eventId);

            if (result.size() == 0) {
                Log.i(DEBUG_TAG, "Event to be deleted: " + eventId);
                Log.i(DEBUG_TAG, "client: " + clientId);
                deleteList.add(eventId);
            }
            taskList.addAll(result);

        }

        cursor.close();


        for (long delEvent : deleteList) {
            if (deleteRow(delEvent)) {
                Log.i(DEBUG_TAG, "Event " + delEvent + " deleted.");
            }
        }
        return taskList;
    }

    private boolean deleteRow(long eventId) {
        String selection = Cols.EVENT_ID + " = " + eventId;
        int deleteResult = mDatabase.delete(DbSchema.TaskTable.NAME, selection, null);
        return deleteResult > 0;
    }


    private Cursor queryEvent(long eventId) {

        String selection = CalendarContract.Events._ID + " = ?";
        String[] selectionARgs = new String[]{Long.toString(eventId)};
        return mContext.getContentResolver().query(CalendarContract.Events.CONTENT_URI, projections,
                selection, selectionARgs, null);

    }

    public static final String[] INSTANCE_PROJECTION = new String[] {
            CalendarContract.Instances.EVENT_ID,      // 0
            CalendarContract.Instances.BEGIN,         // 1
            CalendarContract.Instances.END          // 2
    };

    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_BEGIN_INDEX = 1;
    private static final int PROJECTION_END_INDEX = 2;


    public List<Task> queryInstance(LocalDate date) {
        // query one day
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth(), 0, 0);
        Calendar endTime = Calendar.getInstance();
        endTime.set(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth(), 23, 59);
        List<Task> list =  queryInstance(beginTime, endTime, null);

        for (int i = 0; i < list.size(); i++) {
            Task t = list.get(i);
            Cursor c = mDatabase.query(DbSchema.TaskTable.NAME, new String[]{Cols.EVENT_ID},
                    Cols.EVENT_ID + " = ?",
                    new String[]{Long.toString(t.getId())}, null, null, null);


            if (c == null || c.getCount() == 0) {
                list.remove(i);
            }

            if (c != null) {
                c.close();
            }
        }

        return list;
    }

    public List<Task> queryInstance(Calendar beginTime, Calendar endTime, Long eventId) {

        List<Task> taskList = new ArrayList<>();

        long startMillis = beginTime.getTimeInMillis();
        long endMillis = endTime.getTimeInMillis();

        Cursor cur = null;
        ContentResolver cr = mContext.getContentResolver();


        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);


        String selection = eventId == null ? null : CalendarContract.Instances.EVENT_ID + " = ?";
        String[] selectionArgs = eventId == null ? null : new String[]{Long.toString(eventId)};

        cur = cr.query(builder.build(),
                INSTANCE_PROJECTION,
                selection,
                selectionArgs,
                null);

        if (cur == null) {
            return taskList;
        }

        while (cur.moveToNext()) {
            long begin = 0;
            long end = 0;

            eventId = cur.getLong(PROJECTION_ID_INDEX);
            begin = cur.getLong(PROJECTION_BEGIN_INDEX);
            end = cur.getLong(PROJECTION_END_INDEX);
            Cursor eventCursor = queryEvent(eventId);

            if (eventCursor == null || eventCursor.getCount() == 0) {
                continue;
            }
            eventCursor.moveToFirst();
            Task task = Task.fromCursor(eventCursor);
            task.setStartTime(begin);
            task.setEndTime(end);
            taskList.add(task);
        }

        cur.close();

        return taskList;
    }




    private static class QueryHandler extends AsyncQueryHandler {

        private List<Task> mList;
        ContentResolver mCr;

        public QueryHandler(ContentResolver cr, List<Task> list) {
            super(cr);
            mCr = cr;
            mList = list;
        }
        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            while (cursor.moveToNext()) {
                long eventID = cursor.getLong(PROJECTION_ID_INDEX);
                Cursor cur = queryEvent(eventID);
                if (cur == null || cur.getCount() == 0) {
                    continue;
                }
                cur.moveToFirst();
                Task task = Task.fromCursor(cur);
                mList.add(task);
            }

        }

        private Cursor queryEvent(long eventId) {

            String selection = CalendarContract.Events._ID + " = ?";
            String[] selectionARgs = new String[]{Long.toString(eventId)};
            return mCr.query(CalendarContract.Events.CONTENT_URI, projections,
                    selection, selectionARgs, null);

        }
    }

    private static class EventQueryHandler extends AsyncQueryHandler {

        private Client mClient;

        private List<Task> tasks;
        private LocalDate date;

        public EventQueryHandler(ContentResolver cr, List<Task> list) {
            super(cr);
            tasks = list;

        }

        public void setDate(LocalDate selectedDate) {
            date = selectedDate;
        }

        public void setClient(Client client) {
            mClient = client;
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (cursor == null || cursor.getCount() == 0) {
                return;
            }
            cursor.moveToFirst();


            Task task = Task.fromCursor(cursor);

            tasks.add(task);


            cursor.close();
        }
    }

}


