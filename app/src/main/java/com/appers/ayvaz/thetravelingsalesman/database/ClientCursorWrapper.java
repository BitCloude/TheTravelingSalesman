package com.appers.ayvaz.thetravelingsalesman.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.appers.ayvaz.thetravelingsalesman.Model.Client;
import com.appers.ayvaz.thetravelingsalesman.database.DbSchema.ClientTable.Cols;

import java.util.UUID;

/**
 * Created by D on 027 12/27.
 */
public class ClientCursorWrapper extends CursorWrapper {

    public ClientCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Client getClient() {
        String uuidString = getString(getColumnIndex(Cols.UUID));
        String firstName = getString(getColumnIndex(Cols.FIRST_NAME));
        String lastName = getString(getColumnIndex(Cols.LAST_NAME));
        String firstPhone = getString(getColumnIndex(Cols.FIRST_PHONE));
        String secondPhone = getString(getColumnIndex(Cols.SECOND_PHONE));
        String email = getString(getColumnIndex(Cols.EMAIL));
        String address = getString(getColumnIndex(Cols.ADDRESS));
        String company = getString(getColumnIndex(Cols.COMPANY));
        String note = getString(getColumnIndex(Cols.NOTE));
        boolean stared = getInt(getColumnIndex(Cols.STARED)) == 1;

        Client client = new Client(UUID.fromString(uuidString));
        client.setLastName(lastName);
        client.setFirstName(firstName);
        client.setFirstPhone(firstPhone);
        client.setSecondPhone(secondPhone);
        client.setCompany(company);
        client.setEmail(email);
        client.setNote(note);
        client.setAddress(address);
        client.setStared(stared);

        return client;

    }
}
