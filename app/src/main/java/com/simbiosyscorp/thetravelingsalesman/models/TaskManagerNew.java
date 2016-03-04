package com.simbiosyscorp.thetravelingsalesman.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.simbiosyscorp.thetravelingsalesman.database.DatabaseHelper;

import java.util.Calendar;

/**
 * Created by D on 026 01 26.
 */


public class TaskManagerNew {
    public static final String[] INSTANCE_PROJECTION = new String[]{
            CalendarContract.Instances.EVENT_ID,      // 0
            CalendarContract.Instances.BEGIN,         // 1
            CalendarContract.Instances.END          // 2
    };
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_BEGIN_INDEX = 1;
    private static final int PROJECTION_END_INDEX = 2;
    private static TaskManagerNew mTaskManager;

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
    private final String DEBUG_TAG = "TaskManagerNew debug:";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private TaskManagerNew(Context context) {
        mContext = context;
        mDatabase = new DatabaseHelper(mContext)
                .getWritableDatabase();
    }

    public static TaskManagerNew get(Context context) {
        if (mTaskManager == null) {
            mTaskManager = new TaskManagerNew(context);
        }
        return mTaskManager;
    }

    public void QueryAndSetView(RecyclerView recyclerView, RecyclerView.Adapter adapter,
                                ProgressBar progressBar,
                                Calendar beginTime, Calendar endTime, Long eventId) {

    }

    /*private class GetTask extends AsyncTask<Calendar, Void, List<Task>> {

        @Override
        protected List<Task> doInBackground(Calendar... params) {

            return TaskManager.get(getContext()).queryInstance(params[0], params[1], null);
        }

        @Override
        protected void onPostExecute(List<Task> tasks) {
            if (mAdapter == null) {
                mAdapter = new TaskReportAdapter(tasks);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.setData(tasks);
                mAdapter.notifyDataSetChanged();
            }

            mProgressBar.setVisibility(View.GONE);
        }
    }*/

    /*public List<Task> queryInstance(Calendar beginTime, Calendar endTime, Long eventId) {

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
    }*/
}
