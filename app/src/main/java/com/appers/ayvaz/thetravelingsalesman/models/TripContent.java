package com.appers.ayvaz.thetravelingsalesman.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appers.ayvaz.thetravelingsalesman.database.DatabaseHelper;
import com.appers.ayvaz.thetravelingsalesman.database.DatabaseHelperTravExp;
import com.appers.ayvaz.thetravelingsalesman.database.DbSchema;
import com.appers.ayvaz.thetravelingsalesman.database.TripCursorWrapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TripContent {

    private static TripContent content;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private TripContent(Context context) {
        mContext = context;
        mDatabase = new DatabaseHelper(mContext).getWritableDatabase();

    }

    public static TripContent get(Context context) {
        if (content == null) {
            content = new TripContent(context);
        }

        return content;
    }
    public static String CalendarToString(Calendar calendar)
    {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String stringCalender = formatter.format(calendar.getTime());
        return stringCalender;
    }

    private static ContentValues getContentValues(Trip trip) {
        ContentValues values = new ContentValues();
       // values.put(DbSchema.TripTable.Cols.TRIP_ID, Integer.toString(trip.getId()));
        values.put(DbSchema.TripTable.Cols.TRIP_CLIENT_ID, trip.getClient_id().toString());
        values.put(DbSchema.TripTable.Cols.TRIP_TYPE, trip.getType());
        values.put(DbSchema.TripTable.Cols.TRIP_FROM, trip.getTrip_from());
        values.put(DbSchema.TripTable.Cols.TRIP_TO, trip.getTrip_to());
        values.put(DbSchema.TripTable.Cols.TRIP_DATE_FROM, CalendarToString(trip.getDate_from()));
        values.put(DbSchema.TripTable.Cols.TRIP_DATE_TO, CalendarToString(trip.getDate_to()));
        values.put(DbSchema.TripTable.Cols.TRIP_BOARDING,trip.getBoarding());
        values.put(DbSchema.TripTable.Cols.TRIP_DESCRIPTION, trip.getDescription());
        values.put(DbSchema.TripTable.Cols.TRIP_IMAGE, trip.getImage());

        return values;
    }

    public Trip getTrip(int id) {
        TripCursorWrapper cursor = queryTrips(
                DbSchema.TripTable.Cols.TRIP_ID + " = ?",
                new String[]{Integer.toString(id)}, null
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getTrip();
        } finally {
            cursor.close();
        }
    }

    public List<Trip> getTrips() {
        List<Trip> trips = new ArrayList<>();
        String whereClause = null;
        String[] whereArgs = null;
        String sortOrder = null;


        try (TripCursorWrapper cursor = queryTrips(whereClause, whereArgs,
                sortOrder)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                trips.add(cursor.getTrip());
                cursor.moveToNext();
            }
        }

        return trips;
    }

    public List<Trip> getClientTrips(UUID uuid) {
        List<Trip> trips = new ArrayList<>();
        String whereClause  = DbSchema.TripTable.Cols.TRIP_CLIENT_ID + " = ?";
        String[] whereArgs = new String[]{uuid.toString()};
        String sortOrder = null;




        try (TripCursorWrapper cursor = queryTrips(whereClause, whereArgs,
                sortOrder)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                trips.add(cursor.getTrip());
                cursor.moveToNext();
            }
        }

        return trips;
    }


    public void addTrip(Trip item) {
        ContentValues values = getContentValues(item);
        mDatabase.insert(DbSchema.TripTable.NAME, null, values);
    }

    public void updateTrip(Trip trip ) {
        String id = Integer.toString(trip.getId());
        ContentValues values = getContentValues(trip);

        mDatabase.update(DbSchema.TripTable.NAME, values,
                DbSchema.TripTable.Cols.TRIP_ID + " =  ?",
                new String[]{id});
    }

    private TripCursorWrapper queryTrips(String whereClause, String[] whereArgs,
                                               String sortOrder) {
        Cursor cursor = mDatabase.query(
                DbSchema.TripTable.NAME, null, whereClause, whereArgs, null, null, sortOrder
        );

        return new TripCursorWrapper(cursor);
    }


    public boolean delete(int id) {
        String whereClause = DbSchema.TripTable.Cols.TRIP_ID + " = ?";
        String[] whereArgs = new String[]{Integer.toString(id)};
        return mDatabase.delete(DbSchema.TripTable.NAME, whereClause, whereArgs) > 0;
    }
}