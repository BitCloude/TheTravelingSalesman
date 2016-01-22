package com.appers.ayvaz.thetravelingsalesman.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DatabaseHelperTravExp extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "TravelExpenseBase.db";
    private static final String TYPE_BLOB = " BLOB";
    private static final String PRIMARY_KEY_TRIP = DbSchemaTravExp.TripTable.Cols.TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";
    private static final String PRIMARY_KEY_EXPENSE = DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";


    public DatabaseHelperTravExp(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String expenseSql = String.format("create table %s " +
                        "( %s, %s, %s, %s, %s, %s, %s, %s, %s)",
                DbSchemaTravExp.ExpenseTable.NAME,
                PRIMARY_KEY_EXPENSE,
                DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_TRIP_ID,          //foreign key
                DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_CLIENT_ID,        //foreign key
                DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_TYPE,
                DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_AMOUNT,
                DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_DATE_FROM,
                DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_DATE_TO,
                DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_DESCRIPTION,
                DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_IMAGE + TYPE_BLOB
        );
        db.execSQL(expenseSql);

        String tripSql = String.format("create table %s " +
                        "( %s, %s, %s, %s, %s, %s, %s, %s, %s)",
                DbSchemaTravExp.TripTable.NAME,
                PRIMARY_KEY_TRIP,
                DbSchemaTravExp.TripTable.Cols.TRIP_CLIENT_ID,          //foreign key
                DbSchemaTravExp.TripTable.Cols.TRIP_TYPE,
                DbSchemaTravExp.TripTable.Cols.TRIP_FROM,
                DbSchemaTravExp.TripTable.Cols.TRIP_TO,
                DbSchemaTravExp.TripTable.Cols.TRIP_DATE_FROM,
                DbSchemaTravExp.TripTable.Cols.TRIP_DATE_TO,
                DbSchemaTravExp.TripTable.Cols.TRIP_DESCRIPTION,
                DbSchemaTravExp.TripTable.Cols.TRIP_IMAGE + TYPE_BLOB
        );

        db.execSQL(tripSql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
