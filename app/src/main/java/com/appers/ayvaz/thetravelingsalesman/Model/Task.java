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

    public Calendar getStartDate() {
        return startDate;
    }

    public UUID getId() {
        return mId;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    private Calendar startDate;
    private Calendar endDate;

    public String name;
    public String note;
    public String fromDate;
    public String toDate;

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

    public Task() {
        mId = UUID.randomUUID();
        startDate = new GregorianCalendar();
        endDate = new GregorianCalendar();
        note = "Do something...........................................";
        name = "Anthony Cashmore";
    }

    public Task(String fromDate, String toDate) {
        this.name = "Anthony Cashmore";
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.note = "Do something...........................................";
    }
}
