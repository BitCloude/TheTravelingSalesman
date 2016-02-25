package com.appers.ayvaz.thetravelingsalesman.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.appers.ayvaz.thetravelingsalesman.database.DatabaseHelper;
import com.appers.ayvaz.thetravelingsalesman.database.DbSchema;
import com.appers.ayvaz.thetravelingsalesman.database.DbSchema.ExpenseTable;
import com.appers.ayvaz.thetravelingsalesman.database.ExpenseCursorWrapper;

import org.joda.time.LocalDate;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

//the database utility for expense

public class ExpenseContent {

    private static ExpenseContent content;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private ExpenseContent(Context context) {
        mContext = context;
        mDatabase = new DatabaseHelper(mContext).getWritableDatabase();

    }

    public static ExpenseContent get(Context context) {
        if (content == null) {
            content = new ExpenseContent(context);
        }

        return content;
    }

    public static String CalendarToString(Calendar calendar)
    {
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String stringCalender = formatter.format(calendar.getTime());
        return stringCalender;
    }

    private static ContentValues getContentValues(Expense expense) {
        ContentValues values = new ContentValues();
        //values.put(DbSchema.ExpenseTable.Cols.EXPENSE_ID, Integer.toString(expense.getId()));
        values.put(ExpenseTable.Cols.EXPENSE_TRIP_ID, Integer.toString(expense.getTrip_id()));
        values.put(ExpenseTable.Cols.EXPENSE_CLIENT_ID, expense.getClient_id().toString());
        values.put(ExpenseTable.Cols.EXPENSE_TYPE, expense.getType());
        values.put(ExpenseTable.Cols.EXPENSE_AMOUNT, expense.getAmount());
        values.put(ExpenseTable.Cols.EXPENSE_DATE_FROM, CalendarToString(expense.getDate_from()));
        values.put(ExpenseTable.Cols.EXPENSE_DATE_TO, CalendarToString(expense.getDate_to()));
        values.put(ExpenseTable.Cols.EXPENSE_DESCRIPTION, expense.getDescription());
        values.put(ExpenseTable.Cols.EXPENSE_IMAGE_FILE, expense.getImageFile());

        return values;
    }

    public Expense getExpense(int id) {
        ExpenseCursorWrapper cursor = queryExpenses(
                ExpenseTable.Cols.EXPENSE_ID + " = ?",
                new String[]{Integer.toString(id)}, null
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getExpense();
        } finally {
            cursor.close();
        }
    }

    public List<Expense> getExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String whereClause = null;
        String[] whereArgs = null;
        String sortOrder = ExpenseTable.Cols.EXPENSE_DATE_FROM + " DESC";


        try (ExpenseCursorWrapper cursor = queryExpenses(whereClause, whereArgs,
                sortOrder)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                expenses.add(cursor.getExpense());
                cursor.moveToNext();
            }
        }

        return expenses;
    }

    public List<Expense> getClientExpenses(UUID uuid) {
        List<Expense> expenses = new ArrayList<>();
        String whereClause  = ExpenseTable.Cols.EXPENSE_CLIENT_ID + " = ?";
        String[] whereArgs = new String[]{uuid.toString()};
        String sortOrder = ExpenseTable.Cols.EXPENSE_DATE_FROM + " DESC";

        try (ExpenseCursorWrapper cursor = queryExpenses(whereClause, whereArgs,
                sortOrder)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                expenses.add(cursor.getExpense());
                cursor.moveToNext();
            }
        }

        return expenses;
    }

    public int addExpense(Expense item) {
        ContentValues values = getContentValues(item);
        return (int) mDatabase.insert(ExpenseTable.NAME, null, values);
    }

    public void updateExpense(Expense expense) {
        String id = Integer.toString(expense.getId());
        ContentValues values = getContentValues(expense);

        mDatabase.update(ExpenseTable.NAME, values,
                ExpenseTable.Cols.EXPENSE_ID + " =  ?",
                new String[]{id});
    }

    private ExpenseCursorWrapper queryExpenses(String whereClause, String[] whereArgs,
                                             String sortOrder) {
        Cursor cursor = mDatabase.query(
                ExpenseTable.NAME, null, whereClause, whereArgs, null, null, sortOrder
        );

        return new ExpenseCursorWrapper(cursor);
    }

    public File getPhotoFile(Expense expense, boolean tmp) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }

        Log.i("......", "Path: " + externalFilesDir.getPath());
        return new File(externalFilesDir, expense.getPhotoFileName(tmp));
    }

    private int getMaxID()
    {
        Cursor cursor = mDatabase.rawQuery("SELECT MAX(expense_id) FROM DbSchema.ExpenseTable.NAME", null);
        cursor.moveToFirst();
        int maxID = Integer.valueOf(cursor.getString(0));
        return maxID;
    }

    public boolean delete(int id) {
        String whereClause = ExpenseTable.Cols.EXPENSE_ID + " = ?";
        String[] whereArgs = new String[]{Integer.toString(id)};
        return mDatabase.delete(ExpenseTable.NAME, whereClause, whereArgs) > 0;
    }

    public List<Expense> getTripExpenses(int tripId, Calendar start, Calendar end) {
        List<Expense> result = new ArrayList<>();
//        LocalDate startDate = LocalDate.fromCalendarFields(start);
//        LocalDate endDate = LocalDate.fromCalendarFields(end);

        try (ExpenseCursorWrapper cursor = queryExpenses(ExpenseTable.Cols.EXPENSE_TRIP_ID + " = ?"
        , new String[]{Integer.toString(tripId)}, null)) {
            if (cursor == null) {
                return result;
            }

            while (cursor.moveToNext()) {
                Expense e = cursor.getExpense();
//                LocalDate current = LocalDate.fromCalendarFields(e.getDate_from());
//                if (current.isBefore(startDate) || current.isAfter(endDate)) {
//                    continue;
//                }
                result.add(e);
            }
        }
            catch (Exception e) {
            e.printStackTrace();
        }

        return result;


    }
}
