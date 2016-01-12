package com.appers.ayvaz.thetravelingsalesman.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appers.ayvaz.thetravelingsalesman.database.DbSchema.ClientTable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "clientBase.db";
    private static final String TYPE_INT = " INT";
    private static final String TYPE_BLOB = " BLOB";
    private static final String PRIMARY_KEY = "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s " +
                        "( %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
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
                ClientTable.Cols.IMAGE + TYPE_BLOB
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


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
