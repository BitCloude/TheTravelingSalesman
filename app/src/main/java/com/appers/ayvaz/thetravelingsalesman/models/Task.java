package com.appers.ayvaz.thetravelingsalesman.models;

import android.database.Cursor;
import android.provider.CalendarContract.Events;

import com.appers.ayvaz.thetravelingsalesman.utils.DateTimeHelper;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

/**
 * Created by D on 12/13/2015.
 */
public class Task {



    private long mId;
    private Date startTime;
    private Date endTime;
    private boolean hasAlarm;
    private String notes;
    private String title;
    private String location;
    private boolean hasAttendee;
    private Client client;


    public UUID getClientID() { return client.getId(); }

    public String getTitle() {
        return title;
    }

    public String getNotes() {
        return notes;
    }

    public boolean hasAlarm() {
        return hasAlarm;
    }

    public boolean hasAttendee() {
        return hasAttendee;
    }

    public boolean hasNotes() {
        return notes != null && !notes.equals("");
    }





    public Task() {

    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setStartTime(long millis) {
        this.startTime = DateTimeHelper.fromMillis(millis);
    }

    public void setEndTime(long millis) {
        this.endTime = DateTimeHelper.fromMillis(millis);
    }

    public Task(long eventId) {
        mId = eventId;
    }

    public Client getClient() {
        return client;
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
        this.client = client;
    }


    public String getLocation() {return location;}



    private void setByDate(Date date, Calendar target) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        target.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        target.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        target.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
    }

    public static Task fromCursor(Cursor eventCursor) {
        Task task = new Task();

        task.mId = eventCursor.getLong(eventCursor.getColumnIndex(
                Events._ID));
        task.startTime = DateTimeHelper.fromContentResolver(eventCursor.getString(
                eventCursor.getColumnIndex(Events.DTSTART)));
        task.endTime = DateTimeHelper.fromContentResolver(eventCursor.getString(
                eventCursor.getColumnIndex(Events.DTEND)));
        task.hasAlarm = eventCursor.getInt(eventCursor.getColumnIndex(
                Events.HAS_ALARM)) > 0;
        task.hasAttendee = eventCursor.getInt(eventCursor.getColumnIndex(
                Events.HAS_ATTENDEE_DATA)) > 0;
        task.notes = eventCursor.getString(eventCursor.getColumnIndex(
                Events.DESCRIPTION));
        task.title = eventCursor.getString(eventCursor.getColumnIndex(
                Events.TITLE));
        task.location = eventCursor.getString(eventCursor.getColumnIndex(
                Events.EVENT_LOCATION));

        return task;

    }


    public static class TitleComparator implements Comparator<Task> {

        @Override
        public int compare(Task lhs, Task rhs) {
            return lhs.getTitle().compareTo(rhs.getTitle());
        }
    }

    public static class DateTimeComparator implements Comparator<Task> {


        @Override
        public int compare(Task lhs, Task rhs) {
            return lhs.getStartTime().compareTo(rhs.getStartTime());
        }
    }

    public static class TimeComparator implements Comparator<Task> {
        @Override
        public int compare(Task lhs, Task rhs) {
            return DateTimeHelper.compare(lhs.getStartTime(), rhs.getStartTime());
        }

    }
}


