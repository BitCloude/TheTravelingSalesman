package com.appers.ayvaz.thetravelingsalesman.Model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by D on 12/13/2015.
 */
public class Task {
    private UUID mId;

    public Date getStartDate() {
        return startDate;
    }

    public UUID getId() {
        return mId;
    }

    public Date getEndDate() {
        return endDate;
    }

    private Date startDate;
    private Date endDate;

    public String name;
    public String note;
    public String fromDate;
    public String toDate;

    public Task() {
        mId = UUID.randomUUID();
        startDate = new Date();
        endDate = new Date();
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
