package com.appers.ayvaz.thetravelingsalesman.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperTravExp extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "TravelExpenseBase.db";
    private static final String TYPE_BLOB = " BLOB";
    private static final String PRIMARY_KEY_TRIP = DbSchema.TripTable.Cols.TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";
    private static final String PRIMARY_KEY_EXPENSE = DbSchema.ExpenseTable.Cols.EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";


    public DatabaseHelperTravExp(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String expenseSql = String.format("create table %s " +
                        "( %s, %s, %s, %s, %s, %s, %s, %s, %s, FOREIGN KEY(%s) REFERENCES %s(%s),FOREIGN KEY(%s) REFERENCES %s(%s))",
                DbSchema.ExpenseTable.NAME,
                PRIMARY_KEY_EXPENSE,
                DbSchema.ExpenseTable.Cols.EXPENSE_TRIP_ID,          //foreign key
                DbSchema.ExpenseTable.Cols.EXPENSE_CLIENT_ID,        //foreign key
                DbSchema.ExpenseTable.Cols.EXPENSE_TYPE,
                DbSchema.ExpenseTable.Cols.EXPENSE_AMOUNT,
                DbSchema.ExpenseTable.Cols.EXPENSE_DATE_FROM,
                DbSchema.ExpenseTable.Cols.EXPENSE_DATE_TO,
                DbSchema.ExpenseTable.Cols.EXPENSE_DESCRIPTION,
                DbSchema.ExpenseTable.Cols.EXPENSE_IMAGE + TYPE_BLOB,
                DbSchema.ExpenseTable.Cols.EXPENSE_TRIP_ID,
                DbSchema.TripTable.NAME,
                DbSchema.TripTable.Cols.TRIP_ID,
                DbSchema.ExpenseTable.Cols.EXPENSE_CLIENT_ID,
                DbSchema.ClientTable.NAME,
                DbSchema.ClientTable.Cols.UUID

                );
        db.execSQL(expenseSql);

        String tripSql = String.format("create table %s " +
                        "( %s, %s, %s, %s, %s, %s, %s, %s, %s, FOREIGN KEY(%s) REFERENCES %s(%s))",
                DbSchema.TripTable.NAME,
                PRIMARY_KEY_TRIP,
                DbSchema.TripTable.Cols.TRIP_CLIENT_ID,          //foreign key
                DbSchema.TripTable.Cols.TRIP_TYPE,
                DbSchema.TripTable.Cols.TRIP_FROM,
                DbSchema.TripTable.Cols.TRIP_TO,
                DbSchema.TripTable.Cols.TRIP_DATE_FROM,
                DbSchema.TripTable.Cols.TRIP_DATE_TO,
                DbSchema.TripTable.Cols.TRIP_DESCRIPTION,
                DbSchema.TripTable.Cols.TRIP_IMAGE + TYPE_BLOB,
                DbSchema.TripTable.Cols.TRIP_CLIENT_ID,
                DbSchema.ClientTable.NAME,
                DbSchema.ClientTable.Cols.UUID
                );

        db.execSQL(tripSql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
