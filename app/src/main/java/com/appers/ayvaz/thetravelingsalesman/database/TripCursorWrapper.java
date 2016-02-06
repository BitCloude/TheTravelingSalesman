package com.appers.ayvaz.thetravelingsalesman.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.appers.ayvaz.thetravelingsalesman.models.Trip;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class TripCursorWrapper extends CursorWrapper {

    public TripCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public static boolean isUUIDValid(String uuid){
        if( uuid == null) return false;
        try {
            // we have to convert to object and back to string because the built in fromString does not have
            // good validation logic.
            UUID fromStringUUID = UUID.fromString(uuid);
            String toStringUUID = fromStringUUID.toString();
            return toStringUUID.equals(uuid);
        } catch(IllegalArgumentException e) {
            return false;
        }
    }

    public static Calendar stringToCalendar(String stringDate)
    {
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(formatter.parse(stringDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        */
        return calendar;
    }

    public Trip getTrip() {
        String id = getString(getColumnIndex(DbSchema.TripTable.Cols.TRIP_ID));
        String client_id = getString(getColumnIndex(DbSchema.TripTable.Cols.TRIP_CLIENT_ID));
        String type = getString(getColumnIndex(DbSchema.TripTable.Cols.TRIP_TYPE));
        String trip_from = getString(getColumnIndex(DbSchema.TripTable.Cols.TRIP_FROM));
        String trip_to = getString(getColumnIndex(DbSchema.TripTable.Cols.TRIP_TO));
        String date_from = getString(getColumnIndex(DbSchema.TripTable.Cols.TRIP_DATE_FROM));
        String date_to = getString(getColumnIndex(DbSchema.TripTable.Cols.TRIP_DATE_TO));
        String boarding = getString(getColumnIndex(DbSchema.TripTable.Cols.TRIP_BOARDING));
        String description = getString(getColumnIndex(DbSchema.TripTable.Cols.TRIP_DESCRIPTION));
        byte[] img = getBlob(getColumnIndex(DbSchema.TripTable.Cols.TRIP_IMAGE));

        Trip trip = new Trip(Integer.valueOf(id));
        //Trip trip = new Trip();
        if(isUUIDValid(client_id)){
        trip.setClient_id(UUID.fromString(client_id));}
        trip.setType(type);
        trip.setTrip_from(trip_from);
        trip.setTrip_to(trip_to);
        trip.setDate_from(stringToCalendar(date_from));
        trip.setDate_to(stringToCalendar(date_to));
        trip.setBoarding(boarding);
        trip.setDescription(description);
        trip.setImage(img);

        return trip;

    }
}
