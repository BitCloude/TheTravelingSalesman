package com.appers.ayvaz.thetravelingsalesman.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.appers.ayvaz.thetravelingsalesman.database.DatabaseHelperTravExp;
import com.appers.ayvaz.thetravelingsalesman.database.DbSchemaTravExp;
import com.appers.ayvaz.thetravelingsalesman.database.ExpenseCursorWrapper;

import java.util.ArrayList;
import java.util.List;

//the database utility for expense

public class ExpenseContent {

    private static ExpenseContent content;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private ExpenseContent(Context context) {
        mContext = context;
        mDatabase = new DatabaseHelperTravExp(mContext).getWritableDatabase();

    }

    public static ExpenseContent get(Context context) {
        if (content == null) {
            content = new ExpenseContent(context);
        }

        return content;
    }

    private static ContentValues getContentValues(Expense expense) {
        ContentValues values = new ContentValues();
        values.put(DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_ID, Integer.toString(expense.getId()));
        values.put(DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_TRIP_ID, Integer.toString(expense.getTrip_id()));
        values.put(DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_CLIENT_ID, Integer.toString(expense.getClient_id()));
        values.put(DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_TYPE, expense.getType());
        values.put(DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_AMOUNT, expense.getAmount());
        values.put(DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_DATE_FROM, expense.getDate_from());
        values.put(DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_DATE_TO, expense.getDate_to());
        values.put(DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_DESCRIPTION, expense.getDescription());
        values.put(DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_IMAGE, expense.getImage());

        return values;
    }

    public Expense getExpense(int id) {
        ExpenseCursorWrapper cursor = queryExpenses(
                DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_ID + " = ?",
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
        String sortOrder = DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_ID;


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
        mDatabase.insert(DbSchemaTravExp.ExpenseTable.NAME, null, values);
    }

    public void updateExpense(Expense expense) {
        String id = Integer.toString(expense.getId());
        ContentValues values = getContentValues(expense);

        mDatabase.update(DbSchemaTravExp.ExpenseTable.NAME, values,
                DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_ID + " =  ?",
                new String[]{id});
    }

    private ExpenseCursorWrapper queryExpenses(String whereClause, String[] whereArgs,
                                             String sortOrder) {
        Cursor cursor = mDatabase.query(
                DbSchemaTravExp.ExpenseTable.NAME, null, whereClause, whereArgs, null, null, sortOrder
        );

        return new ExpenseCursorWrapper(cursor);
    }


    public boolean delete(int id) {
        String whereClause = DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_ID + " = ?";
        String[] whereArgs = new String[]{Integer.toString(id)};
        return mDatabase.delete(DbSchemaTravExp.ExpenseTable.NAME, whereClause, whereArgs) > 0;
    }
}
