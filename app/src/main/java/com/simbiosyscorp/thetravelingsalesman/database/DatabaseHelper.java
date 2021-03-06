package com.simbiosyscorp.thetravelingsalesman.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.simbiosyscorp.thetravelingsalesman.database.DbSchema.ClientTable;

public class DatabaseHelper extends SQLiteOpenHelper {
    // version 2 : add client's link to contact book
    private static final int VERSION = 3;
    private static final String DATABASE_NAME = "clientBase.db";
    private static final String TYPE_INT = " INT";
    private static final String TYPE_BLOB = " BLOB";
    private static final String PRIMARY_KEY = "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";
    private static final String PRIMARY_KEY_TRIP = DbSchema.TripTable.Cols.TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";
    private static final String PRIMARY_KEY_EXPENSE = DbSchema.ExpenseTable.Cols.EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s " +
                        "( %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
                ClientTable.NAME,
                PRIMARY_KEY,
                ClientTable.Cols.UUID,
                ClientTable.Cols.STARED + TYPE_INT,
                ClientTable.Cols.FIRST_NAME,
                ClientTable.Cols.LAST_NAME,
                ClientTable.Cols.FIRST_PHONE,
                ClientTable.Cols.SECOND_PHONE,
                ClientTable.Cols.EMAIL,
                ClientTable.Cols.ADDRESS,
                ClientTable.Cols.COMPANY,
                ClientTable.Cols.LINKEDIN,
                ClientTable.Cols.NOTE,
                ClientTable.Cols.CONTACT_ID,
                ClientTable.Cols.DESIGNATION
                );
        db.execSQL(sql);

        String taskSql = String.format("create table %s " +
        "( %s, %s, %s, FOREIGN KEY(%s) REFERENCES %s(%s))",
                DbSchema.TaskTable.NAME,
                PRIMARY_KEY,
                DbSchema.TaskTable.Cols.EVENT_ID,
                DbSchema.TaskTable.Cols.CLIENT_ID,
                DbSchema.TaskTable.Cols.CLIENT_ID,
                ClientTable.NAME,
                ClientTable.Cols.UUID
                );

        db.execSQL(taskSql);

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
                DbSchema.ExpenseTable.Cols.EXPENSE_IMAGE_FILE,
                DbSchema.ExpenseTable.Cols.EXPENSE_TRIP_ID,
                DbSchema.TripTable.NAME,
                DbSchema.TripTable.Cols.TRIP_ID,
                DbSchema.ExpenseTable.Cols.EXPENSE_CLIENT_ID,
                DbSchema.ClientTable.NAME,
                DbSchema.ClientTable.Cols.UUID

        );
        db.execSQL(expenseSql);

        String tripSql = String.format("create table %s " +
                        "( %s, %s, %s, %s, %s, %s, %s, %s, %s, %s,  FOREIGN KEY(%s) REFERENCES %s(%s))",
                DbSchema.TripTable.NAME,
                PRIMARY_KEY_TRIP,
                DbSchema.TripTable.Cols.TRIP_CLIENT_ID,          //foreign key
                DbSchema.TripTable.Cols.TRIP_TYPE,
                DbSchema.TripTable.Cols.TRIP_FROM,
                DbSchema.TripTable.Cols.TRIP_TO,
                DbSchema.TripTable.Cols.TRIP_DATE_FROM,
                DbSchema.TripTable.Cols.TRIP_DATE_TO,
                DbSchema.TripTable.Cols.TRIP_BOARDING,
                DbSchema.TripTable.Cols.TRIP_DESCRIPTION,
                DbSchema.TripTable.Cols.TRIP_IMAGE_FILE,
                DbSchema.TripTable.Cols.TRIP_CLIENT_ID,
                DbSchema.ClientTable.NAME,
                DbSchema.ClientTable.Cols.UUID
        );

        db.execSQL(tripSql);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            String query = String.format("Alter TABLE %s ADD COLUMN %s",
                    ClientTable.NAME,
                    ClientTable.Cols.CONTACT_ID);
            db.execSQL(query);
        }

        if (newVersion == 3) {
            db.execSQL(String.format("Alter TABLE %s ADD COLUMN %s",
                    ClientTable.NAME,
                    ClientTable.Cols.DESIGNATION));
        }

    }


}
