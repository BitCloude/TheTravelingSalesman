package com.appers.ayvaz.thetravelingsalesman.Model;

import android.database.Cursor;
import android.provider.CallLog.Calls;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by D on 007 01/07.
 */
public class CallEntry {
    private String number;
    private Date time;
    private int type;

    public CallEntry() {

    }

    public String getNumber() {
        return number;
    }

    public Date getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    public static CallEntry fromCursor(Cursor cursor) {
        CallEntry call = new CallEntry();
        call.number = cursor.getString(cursor.getColumnIndex(Calls.NUMBER));
        call.type = cursor.getInt(cursor.getColumnIndex(Calls.TYPE));
        String date = cursor.getString(cursor.getColumnIndex(Calls.DATE));
        Long timestamp = Long.parseLong(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        call.time = calendar.getTime();

        return call;
    }
}
