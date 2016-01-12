package com.appers.ayvaz.thetravelingsalesman.models;

import android.database.Cursor;
import android.provider.CalendarContract;

import com.appers.ayvaz.thetravelingsalesman.utils.DateTimeHelper;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by D on 12/13/2015.
 */
public class TaskII {



    private long mId;




    private Date startTime;
    private Date endTime;
    private boolean hasAlarm;

    public boolean hasAlarm() {
        return hasAlarm;
    }

    public boolean hasAttendee() {
        return hasAttendee;
    }

    public boolean hasExtendedProperties() {
        return hasExtendedProperties;
    }

    private boolean hasAttendee;
    private boolean hasExtendedProperties;
    private Client mClient;



    public TaskII() {

    }

    public TaskII(long eventId) {
        mId = eventId;
    }

    public Client getClient() {
        return mClient;
    }

    public long getId() {
        return mId;
    }

    public Date getStartTime() {
        return startTime;
    }


    public Date getEndTime() {
        return endTime;
    }

    public void setClient(Client client) {
        mClient = client;
    }



    private void setByDate(Date date, Calendar target) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        target.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        target.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        target.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
    }

    public static TaskII fromCursor(Cursor eventCursor) {
        TaskII taskII = new TaskII();

        taskII.mId = eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Events._ID));
        taskII.startTime = DateTimeHelper.fromContentResolver(eventCursor.getString(
                eventCursor.getColumnIndex(CalendarContract.Events.DTSTART)));
        taskII.endTime = DateTimeHelper.fromContentResolver(eventCursor.getString(
                eventCursor.getColumnIndex(CalendarContract.Events.DTEND)));
        taskII.hasAlarm = eventCursor.getInt(
                eventCursor.getColumnIndex(CalendarContract.Events.HAS_ALARM)) > 0;
        taskII.hasAttendee = eventCursor.getInt(
                eventCursor.getColumnIndex(CalendarContract.Events.HAS_ATTENDEE_DATA)) > 0;
        taskII.hasExtendedProperties = eventCursor.getInt(
                eventCursor.getColumnIndex(CalendarContract.Events.HAS_EXTENDED_PROPERTIES)) > 0;

        return taskII;

    }





}
