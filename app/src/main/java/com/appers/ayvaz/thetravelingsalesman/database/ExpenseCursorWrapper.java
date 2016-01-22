package com.appers.ayvaz.thetravelingsalesman.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.appers.ayvaz.thetravelingsalesman.models.Expense;

public class ExpenseCursorWrapper extends CursorWrapper {

    public ExpenseCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Expense getExpense() {
        String id = getString(getColumnIndex(DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_ID));
        String trip_id = getString(getColumnIndex(DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_TRIP_ID));
        String client_id = getString(getColumnIndex(DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_CLIENT_ID));
        String type = getString(getColumnIndex(DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_TYPE));
        String amount = getString(getColumnIndex(DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_AMOUNT));
        String date_from = getString(getColumnIndex(DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_DATE_FROM));
        String date_to = getString(getColumnIndex(DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_DATE_TO));
        String description = getString(getColumnIndex(DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_DESCRIPTION));
        byte[] img = getBlob(getColumnIndex(DbSchemaTravExp.ExpenseTable.Cols.EXPENSE_IMAGE));

        Expense expense = new Expense(Integer.valueOf(id));
        expense.setTrip_id(Integer.valueOf(trip_id));
        expense.setClient_id(Integer.valueOf(client_id));
        expense.setType(type);
        expense.setAmount(amount);
        expense.setDate_from(date_from);
        expense.setDate_to(date_to);
        expense.setDescription(description);
        expense.setImage(img);

        return expense;

    }
}

