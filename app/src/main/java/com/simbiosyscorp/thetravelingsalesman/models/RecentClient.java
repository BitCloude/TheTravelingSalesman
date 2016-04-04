package com.simbiosyscorp.thetravelingsalesman.models;

import com.simbiosyscorp.thetravelingsalesman.database.DbSchema;

import java.util.UUID;

/**
 * Created by D on 021 03 21.
 */
public class RecentClient extends Client {
    private long millisecond;

    public RecentClient(Client client) {
        super(client.getId());
        setLastName(client.getLastName());
        setFirstName(client.getFirstName());
        setFirstPhone(client.getFirstPhone());
        setSecondPhone(client.getSecondPhone());
        setCompany(client.getCompany());
        setEmail(client.getEmail());
        setNote(client.getNote());
        setAddress(client.getAddress());
        setStared(client.isStared());
        setLinkedIn(client.getLinkedIn());
        setContactId(client.getContactId());
    }
    public void setMillisecond(long time) {
        this.millisecond = time;
    }

    public long getMillisecond() {
        return millisecond;
    }
}
