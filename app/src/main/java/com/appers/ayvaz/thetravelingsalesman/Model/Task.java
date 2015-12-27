package com.appers.ayvaz.thetravelingsalesman.Model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by D on 12/13/2015.
 */
public class Task {
    private UUID mId;



    private Calendar startDate;
    private Calendar endDate;

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
        startDate = GregorianCalendar.getInstance();
        endDate = GregorianCalendar.getInstance();
//        startDate.setTime(new Date());
//        endDate.setTime(new Date());

        note = mId.toString().substring(0, 3) + "Do something...........................................";
        name = "Anthony Cashmore";
    }

    public Calendar getStartDate() {
        return startDate;
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

    public Calendar getEndDate() {
        return endDate;
    }

    public String getNote() {
        return note;
    }
    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public void setStartDate(Date date) {
        setByDate(date, startDate);
    }

    private void setByDate(Date date, Calendar target) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        target.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        target.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        target.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public void setEndDate(Date date) {
        setByDate(date, endDate);
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
