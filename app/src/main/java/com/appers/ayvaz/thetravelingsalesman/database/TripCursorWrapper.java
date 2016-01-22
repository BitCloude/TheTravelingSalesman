package com.appers.ayvaz.thetravelingsalesman.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.appers.ayvaz.thetravelingsalesman.models.Trip;

public class TripCursorWrapper extends CursorWrapper {

    public TripCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Trip getTrip() {
        String id = getString(getColumnIndex(DbSchema.TripTable.Cols.TRIP_ID));
        String client_id = getString(getColumnIndex(DbSchema.TripTable.Cols.TRIP_CLIENT_ID));
        String type = getString(getColumnIndex(DbSchema.TripTable.Cols.TRIP_TYPE));
        String trip_from = getString(getColumnIndex(DbSchema.TripTable.Cols.TRIP_FROM));
        String trip_to = getString(getColumnIndex(DbSchema.TripTable.Cols.TRIP_TO));
        String date_from = getString(getColumnIndex(DbSchema.TripTable.Cols.TRIP_DATE_FROM));
        String date_to = getString(getColumnIndex(DbSchema.TripTable.Cols.TRIP_DATE_TO));
        String description = getString(getColumnIndex(DbSchema.TripTable.Cols.TRIP_DESCRIPTION));
        byte[] img = getBlob(getColumnIndex(DbSchema.TripTable.Cols.TRIP_IMAGE));

        Trip trip = new Trip(Integer.valueOf(id));
        trip.setClient_id(Integer.valueOf(client_id));
        trip.setType(type);
        trip.setTrip_from(trip_from);
        trip.setTrip_to(trip_to);
        trip.setDate_from(date_from);
        trip.setDate_to(date_to);
        trip.setDescription(description);
        trip.setImage(img);

        return trip;

    }
}
