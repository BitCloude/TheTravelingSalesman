package com.appers.ayvaz.thetravelingsalesman.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appers.ayvaz.thetravelingsalesman.database.DatabaseHelper;
import com.appers.ayvaz.thetravelingsalesman.database.DatabaseHelperTravExp;
import com.appers.ayvaz.thetravelingsalesman.database.DbSchema;
import com.appers.ayvaz.thetravelingsalesman.database.ExpenseCursorWrapper;

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
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String stringCalender = formatter.format(calendar.getTime());
        return stringCalender;
    }

    private static ContentValues getContentValues(Expense expense) {
        ContentValues values = new ContentValues();
        //values.put(DbSchema.ExpenseTable.Cols.EXPENSE_ID, Integer.toString(expense.getId()));
        values.put(DbSchema.ExpenseTable.Cols.EXPENSE_TRIP_ID, Integer.toString(expense.getTrip_id()));
        values.put(DbSchema.ExpenseTable.Cols.EXPENSE_CLIENT_ID, expense.getClient_id().toString());
        values.put(DbSchema.ExpenseTable.Cols.EXPENSE_TYPE, expense.getType());
        values.put(DbSchema.ExpenseTable.Cols.EXPENSE_AMOUNT, expense.getAmount());
        values.put(DbSchema.ExpenseTable.Cols.EXPENSE_DATE_FROM, CalendarToString(expense.getDate_from()));
        values.put(DbSchema.ExpenseTable.Cols.EXPENSE_DATE_TO, CalendarToString(expense.getDate_to()));
        values.put(DbSchema.ExpenseTable.Cols.EXPENSE_DESCRIPTION, expense.getDescription());
        values.put(DbSchema.ExpenseTable.Cols.EXPENSE_IMAGE, expense.getImage());

        return values;
    }

    public Expense getExpense(int id) {
        ExpenseCursorWrapper cursor = queryExpenses(
                DbSchema.ExpenseTable.Cols.EXPENSE_ID + " = ?",
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
        String sortOrder = null;


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
        String whereClause  = DbSchema.ExpenseTable.Cols.EXPENSE_CLIENT_ID + " = ?";
        String[] whereArgs = new String[]{uuid.toString()};
        String sortOrder = null;

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

    public void addExpense(Expense item) {
        ContentValues values = getContentValues(item);
        mDatabase.insert(DbSchema.ExpenseTable.NAME, null, values);
    }

    public void updateExpense(Expense expense) {
        String id = Integer.toString(expense.getId());
        ContentValues values = getContentValues(expense);

        mDatabase.update(DbSchema.ExpenseTable.NAME, values,
                DbSchema.ExpenseTable.Cols.EXPENSE_ID + " =  ?",
                new String[]{id});
    }

    private ExpenseCursorWrapper queryExpenses(String whereClause, String[] whereArgs,
                                             String sortOrder) {
        Cursor cursor = mDatabase.query(
                DbSchema.ExpenseTable.NAME, null, whereClause, whereArgs, null, null, sortOrder
        );

        return new ExpenseCursorWrapper(cursor);
    }


    public boolean delete(int id) {
        String whereClause = DbSchema.ExpenseTable.Cols.EXPENSE_ID + " = ?";
        String[] whereArgs = new String[]{Integer.toString(id)};
        return mDatabase.delete(DbSchema.ExpenseTable.NAME, whereClause, whereArgs) > 0;
    }
}
