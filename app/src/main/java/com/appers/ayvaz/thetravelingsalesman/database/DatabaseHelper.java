package com.appers.ayvaz.thetravelingsalesman.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appers.ayvaz.thetravelingsalesman.database.DbSchema.ClientTable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "clientBase.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s " +
                        "(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
                ClientTable.NAME,
                ClientTable.Cols.UUID,
                ClientTable.Cols.STARED,
                ClientTable.Cols.FIRST_NAME,
                ClientTable.Cols.LAST_NAME,
                ClientTable.Cols.FIRST_PHONE,
                ClientTable.Cols.SECOND_PHONE,
                ClientTable.Cols.EMAIL,
                ClientTable.Cols.ADDRESS,
                ClientTable.Cols.COMPANY,
                ClientTable.Cols.NOTE
                );
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
