package com.appers.ayvaz.thetravelingsalesman.models;

import android.database.Cursor;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by D on 030 12/30.
 */
public class MyMessage implements Comparable<MyMessage> {
    @Override
    public int compareTo(MyMessage another) {

        if (time.equals(another.getTime())) {
            return 0;
        }
        return another.getTime().after(time) ? 1 : -1;
    }

    public final static int INBOX = 0;
    public final static int SENT = 1;

    private String body, address;
    private Date time;
    private int type;
    public class Cols {
        public static final String ID = "_id";
        public static final String ADDRESS = "address";
        public static final String DATE = "date";
        public static final String SUBJECT = "subject";
        public static final String BODY = "body";
    }

    public int getType() {
        return type;
    }

    public Date getTime() {
        return time;
    }


    public String getBody() {
        return body;
    }



    public static MyMessage fromCursor(Cursor cursor, int type) {
        MyMessage msg = new MyMessage();
        // // TODO: more attributes
        msg.body = cursor.getString(cursor.getColumnIndex(Cols.BODY));
        msg.address = cursor.getString(cursor.getColumnIndex(Cols.ADDRESS));
        String date = cursor.getString(cursor.getColumnIndex(Cols.DATE));
        Long timestamp = Long.parseLong(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        msg.time = calendar.getTime();
        msg.type = type;
        return msg;
    }
}

