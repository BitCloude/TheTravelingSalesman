package com.simbiosyscorp.thetravelingsalesman.models;

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

import com.simbiosyscorp.thetravelingsalesman.database.DatabaseHelper;
import com.simbiosyscorp.thetravelingsalesman.database.DbSchema;
import com.simbiosyscorp.thetravelingsalesman.database.DbSchema.TaskTable.Cols;

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
    public static final String[] INSTANCE_PROJECTION = new String[]{
            CalendarContract.Instances.EVENT_ID,      // 0
            CalendarContract.Instances.BEGIN,         // 1
            CalendarContract.Instances.END          // 2
    };

    // year range for events
    private final static int END_YEAR = 2030;
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_BEGIN_INDEX = 1;
    private static final int PROJECTION_END_INDEX = 2;
    private static TaskManager mTaskManager;


    private static String[] projections = new String[]{
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.HAS_ALARM,
            CalendarContract.Events.HAS_ATTENDEE_DATA,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.EVENT_LOCATION
    };

    private final String DEBUG_TAG = "TaskManager: ";
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private LocalDate selectedDate;

    private TaskManager(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DatabaseHelper(mContext)
                .getWritableDatabase();
    }

    public static TaskManager get(Context context) {
        if (mTaskManager == null) {
            mTaskManager = new TaskManager(context);
        }
        return mTaskManager;
    }

    public boolean addTask(Task item) {
        ContentValues values = getContentValues(item);
        return mDatabase.insert(DbSchema.TaskTable.NAME, null, values) > 0;
    }

    public boolean delete(long eventID) {
        // delete task from our database
        deleteTask(eventID);
        // delete event from phone
        return deleteEvent(eventID);
    }


    private boolean deleteTask(long eventID) {
        String selection = Cols.EVENT_ID + " = " + eventID;
        int deleteResult = mDatabase.delete(DbSchema.TaskTable.NAME, selection, null);
        Log.i(DEBUG_TAG, "Task " + (deleteResult > 0 ? "" : "not") + " deleted");
        return deleteResult > 0;
    }

    private boolean deleteEvent(long eventID) {
        ContentResolver cr = mContext.getContentResolver();

        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = cr.delete(deleteUri, null, null);
        Log.i(DEBUG_TAG, "Rows deleted: " + rows);

        return rows > 0;
    }

    private Cursor queryEvent(long eventId) {

        String selection = CalendarContract.Events._ID + " = ?";
        String[] selectionARgs = new String[]{Long.toString(eventId)};
        return mContext.getContentResolver().query(CalendarContract.Events.CONTENT_URI, projections,
                selection, selectionARgs, null);

    }

    private ContentValues getContentValues(Task item) {
        ContentValues values = new ContentValues();
        values.put(Cols.CLIENT_ID, item.getClientID().toString());
        values.put(Cols.EVENT_ID, item.getEventID());
        return values;

    }

    public List<Task> query(UUID clientID) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(Long.MAX_VALUE);

        return query(clientID, now, end);
    }

    public List<Task> query(UUID clientID, Calendar start, Calendar end) {
        List<Task> taskList = new ArrayList<>();

        String selection = null;
        String[] selectionArgs = null;

        if (clientID != null) {
            selection = Cols.CLIENT_ID + " = ?";
            selectionArgs = new String[]{clientID.toString()};
        }


        // first, find all eventId that is related to the client
        Cursor cursor = mDatabase.query(
                DbSchema.TaskTable.NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor == null || cursor.getCount() == 0) {
            Log.i("...........", "empty list");
            return taskList;

        }

        // second, for each event that belongs to this client, find all instances in the range
        while (cursor.moveToNext()) {

            long eventId = cursor.getLong(cursor.getColumnIndex(Cols.EVENT_ID));

            List<Task> result = queryInstance(start, end, eventId);



            taskList.addAll(result);

        }

        cursor.close();



        return taskList;
    }

    public void clearTask() {
        List<Long> deleteList = new ArrayList<>();

        List<Long> eventIds = new ArrayList<>();

        try(Cursor cursor = query(null, null)) {
            while (cursor.moveToNext()) {
                eventIds.add(cursor.getLong(cursor.getColumnIndex(Cols.EVENT_ID)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (long eventId : eventIds) {
            Cursor cursor = queryEvent(eventId);
            if (cursor != null && cursor.getCount() == 0) {
                Log.i(DEBUG_TAG, "Event to be deleted: " + eventId);
                deleteList.add(eventId);
            }
        }


        for (long delEvent : deleteList) {
            if (delete(delEvent)) {
                Log.i(DEBUG_TAG, "Event " + delEvent + " deleted.");
            }
        }
    }

    public List<Task> search(CharSequence userInput) {
        String selection = CalendarContract.Events.TITLE + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + userInput + "%"};
        String order = null;

        List<Task> result = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(CalendarContract.Events.CONTENT_URI,
                projections, selection, selectionArgs, order);

        if (cursor == null) {
            return result;
        }

        Client client;

        while (cursor.moveToNext()) {
            Task task = Task.fromCursor(cursor);
            result.add(task);

        }

        cursor.close();

        for (Task t : result) {
            if ((client = getClient(t)) != null) {
                t.setClient(client);
            }
        }

        return result;
    }

    public List<Task> queryInstance(LocalDate date) {
        // query one day
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth(), 0, 0);
        Calendar endTime = Calendar.getInstance();
        endTime.set(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth(), 23, 59);
        List<Task> list = queryInstance(beginTime, endTime, null);
        Client client;

        for (int i = 0; i < list.size(); i++) {
            Task t = list.get(i);
            if ((client = getClient(t)) != null) {
                t.setClient(client);
            }

        }

        return list;
    }

    public List<Task> queryOneDay(Calendar date) {
        // query one day
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH), 0, 0);
        Calendar endTime = Calendar.getInstance();
        endTime.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH), 23, 59);

        List<Task> list = queryInstance(beginTime, endTime, null);



        for (int i = 0; i < list.size(); i++) {
            Task t = list.get(i);
            t.setClient(getClient(t));

        }

        return list;
    }

    private Cursor query(String selection, String[] args) {
        return mDatabase.query(DbSchema.TaskTable.NAME,
                null,
                selection,
                args,
                null, null, null, null);
    }

    private Client getClient(Task t) {
        Cursor c = mDatabase.query(DbSchema.TaskTable.NAME,
                null,
                Cols.EVENT_ID + " = " + t.getEventID(),
                null, null, null, null);
        if (c == null || c.getCount() == 0) {
            return null;
        }

        c.moveToFirst();
        String id = c.getString(c.getColumnIndex(Cols.CLIENT_ID));
        Client client = ClientManager.get(mContext).getClient(UUID.fromString(id));
        c.close();

        return client;
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

        // test AsyncQueryHandler
//
//        EventQueryHandler eqh = new EventQueryHandler(cr, taskList, mContext);
//        eqh.startQuery(0, null, builder.build(),
//                INSTANCE_PROJECTION,
//                selection,
//                selectionArgs,
//                null);

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

    public boolean updateTask(Task task) {
        ContentValues values = getContentValues(task);

        Cursor cursor = mDatabase.query(DbSchema.TaskTable.NAME, null,
                Cols.EVENT_ID + " = " + task.getEventID(), null, null, null, null);
        int cnt = cursor.getCount();
        cursor.close();
        if (cnt > 0) {
            return mDatabase.update(DbSchema.TaskTable.NAME, values,
                    Cols.EVENT_ID + " = " + task.getEventID(),
                    null) > 0;
        } else {
            return mDatabase.insert(DbSchema.TaskTable.NAME, null, values) > 0;
        }


    }





    private static class EventQueryHandler extends AsyncQueryHandler {


        private List<Task> tasks;
        private Context mContext;

        public EventQueryHandler(ContentResolver cr, List<Task> list, Context context) {
            super(cr);
            tasks = list;
            mContext = context;
        }


        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (cursor == null || cursor.getCount() == 0) {
                return;
            }


            while (cursor.moveToNext()) {

                long eventId = cursor.getLong(PROJECTION_ID_INDEX);
                long begin = cursor.getLong(PROJECTION_BEGIN_INDEX);
                long end = cursor.getLong(PROJECTION_END_INDEX);


                String selection = CalendarContract.Events._ID + " = ?";
                String[] selectionARgs = new String[]{Long.toString(eventId)};

                Cursor eventCursor = mContext.getContentResolver().query(
                        CalendarContract.Events.CONTENT_URI, projections,
                        selection, selectionARgs, null);

                if (eventCursor == null || eventCursor.getCount() == 0) {
                    continue;
                }

                eventCursor.moveToFirst();
                Task task = Task.fromCursor(eventCursor);
                task.setStartTime(begin);
                task.setEndTime(end);
                tasks.add(task);

            }

            cursor.close();
        }
    }

    private static class QueryHandler extends AsyncQueryHandler {

        ContentResolver mCr;
        private List<Task> mList;

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

    public MyCalendar [] getCalendar() {

        String projection[] = {"_id", "calendar_displayName"};
        Uri calendars;
        calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor managedCursor = contentResolver.query(calendars, projection, null, null, null);
        MyCalendar[] m_calendars = new MyCalendar[0];

        if (managedCursor != null && managedCursor.moveToFirst()){
             m_calendars = new MyCalendar[managedCursor.getCount()];
            String calName;
            String calID;
            int cont= 0;
            int nameCol = managedCursor.getColumnIndex(projection[1]);
            int idCol = managedCursor.getColumnIndex(projection[0]);
            do {
                calName = managedCursor.getString(nameCol);
                calID = managedCursor.getString(idCol);
                m_calendars[cont] = new MyCalendar(calName, calID);
                cont++;
            } while(managedCursor.moveToNext());
            managedCursor.close();
        }
        return m_calendars;

    }

}

class MyCalendar {
    private String name;
    private String id;

    public MyCalendar(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}


