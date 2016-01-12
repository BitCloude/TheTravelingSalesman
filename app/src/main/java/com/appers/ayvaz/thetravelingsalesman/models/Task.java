package com.appers.ayvaz.thetravelingsalesman.models;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by D on 12/13/2015.
 */
public class Task {


    private UUID mId;
    private long mEventId;



    private Calendar beginTime;
    private Calendar endTime;

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    private String name;
    private String note;
    private String location;
//    public String fromDate;
//    public String toDate;

    public Task() {
        mId = UUID.randomUUID();
//        beginTime = GregorianCalendar.getInstance();
//        endTime = GregorianCalendar.getInstance();
//        startDate.setTime(new Date());
//        endTime.setTime(new Date());

//        note = mId.toString().substring(0, 3) + "Do something...........................................";
//        name = "Anthony Cashmore";
    }

    public Calendar getBeginTime() {
        return beginTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return mId;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public String getNote() {
        return note;
    }
    public void setStartDate(Calendar startDate) {
        this.beginTime = startDate;
    }

    public void setStartDate(Date date) {
        setByDate(date, beginTime);
    }

    private void setByDate(Date date, Calendar target) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        target.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        target.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        target.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public void setEndDate(Date date) {
        setByDate(date, endTime);
    }

    public void setNote(String note) {
        this.note = note;
    }

/*
    public Task(String fromDate, String toDate) {
        this.name = "Anthony Cashmore";
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.note = "Do something...........................................";
    }
*/

}
