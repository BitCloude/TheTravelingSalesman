package com.simbiosyscorp.thetravelingsalesman.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.CalendarContract;

/**
 * Created by D on 009 01/09.
 */
public class EventUtility {
    public static long getNewEventId(ContentResolver cr) {
        Cursor cursor = cr.query(CalendarContract.Events.CONTENT_URI,
                new String [] {"MAX(_id) as max_id"}, null, null, "_id");

        if (cursor == null) {
            return -1;
        }

        cursor.moveToFirst();
        long max_val = cursor.getLong(cursor.getColumnIndex("max_id"));
        cursor.close();
        return max_val + 1;
    }

    public static long getLastEventId(ContentResolver cr) {
        Cursor cursor = cr.query(CalendarContract.Events.CONTENT_URI,
                new String[]{"MAX(_id) as max_id"}, null, null, "_id");
        if (cursor == null || cursor.getCount() == 0) {
            return -1;
        }

        cursor.moveToFirst();
        long max_val = cursor.getLong(cursor.getColumnIndex("max_id"));
        cursor.close();

        return max_val;
    }
}
