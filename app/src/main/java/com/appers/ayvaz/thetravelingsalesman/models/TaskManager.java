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

    public List<TaskII> query(UUID clientId) {
        List<TaskII> taskIIList = new ArrayList<>();


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
            return taskIIList;

        }


//        EventQueryHandler queryHandler = new EventQueryHandler(mContext.getContentResolver(),
//                taskIIList);

        cursor.moveToFirst();

        do {
            long eventId = cursor.getLong(cursor.getColumnIndex(Cols.EVENT_ID));

//            queryHandler.startQuery(1, null, CalendarContract.Events.CONTENT_URI, projections,
//                    CalendarContract.Events._ID + " = ?",
//                    new String[]{Long.toString(eventId)},
//                    null);

            Cursor eventCursor = queryEvent(eventId);
            if (eventCursor == null || eventCursor.getCount() == 0) {
                continue;
            }
            eventCursor.moveToFirst();
            TaskII taskII = TaskII.fromCursor(eventCursor);
//            taskII.setClient(client);
            taskIIList.add(taskII);
            eventCursor.close();

        } while (cursor.moveToNext());
        cursor.close();
        return taskIIList;
    }


    public List<TaskII> query(LocalDate date) {
        List<TaskII> taskIIList = new ArrayList<>();

        selectedDate = date;

        Cursor cursor = mDatabase.query(
                DbSchema.TaskTable.NAME,
                null,
                null,
                null,
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
            UUID clientId = UUID.fromString(cursor.getString(cursor.getColumnIndex(Cols.CLIENT_ID)));
            Cursor eventCursor = queryEvent(eventId);
            if (eventCursor == null || eventCursor.getCount() == 0) {
                continue;
            }
            eventCursor.moveToFirst();
            String s = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.DTSTART));
            Date dtstart = DateTimeHelper.fromContentResolver(s);
            LocalDate dt = new LocalDate(dtstart);
            if (dt.isEqual(date)) {
                TaskII taskII = TaskII.fromCursor(eventCursor);
                UUID id = UUID.fromString(cursor.getString(cursor.getColumnIndex(Cols.CLIENT_ID)));
                Client client = ClientManager.get(mContext).getClient(id);
                taskII.setClient(client);
                taskIIList.add(taskII);
            }
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

    public static final String[] INSTANCE_PROJECTION = new String[] {
            CalendarContract.Instances.EVENT_ID,      // 0
            CalendarContract.Instances.BEGIN,         // 1
            CalendarContract.Instances.TITLE          // 2
    };
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_BEGIN_INDEX = 1;
    private static final int PROJECTION_TITLE_INDEX = 2;

    public List<TaskII> queryByDate(LocalDate date) {
        final List<TaskII> list = new ArrayList<>();

        AsyncQueryHandler handler = new QueryHandler(mContext.getContentResolver(), list);

        Calendar c = Calendar.getInstance();
        c.set(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
        long startMilis = c.getTimeInMillis();
        date = date.plusDays(1);
        c.set(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
        long endMilis = c.getTimeInMillis() - 1;
        Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI
                .buildUpon();
        ContentUris.appendId(eventsUriBuilder, startMilis);
        ContentUris.appendId(eventsUriBuilder, endMilis);

        Uri eventsUri = eventsUriBuilder.build();

        handler.startQuery(1, null, eventsUri, INSTANCE_PROJECTION,
                null, null, CalendarContract.Instances.DTSTART + " ASC");


        return list;


    }

    private static class QueryHandler extends AsyncQueryHandler {

        private List<TaskII> mList;
        ContentResolver mCr;

        public QueryHandler(ContentResolver cr, List<TaskII> list) {
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
                TaskII task = TaskII.fromCursor(cur);
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

        private List<TaskII> tasks;
        private LocalDate date;

        public EventQueryHandler(ContentResolver cr, List<TaskII> list) {
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


            TaskII task = TaskII.fromCursor(cursor);

            tasks.add(task);


            cursor.close();
        }
    }

}


