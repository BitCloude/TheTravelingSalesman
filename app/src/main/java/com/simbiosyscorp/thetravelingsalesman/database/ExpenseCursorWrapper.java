package com.simbiosyscorp.thetravelingsalesman.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.simbiosyscorp.thetravelingsalesman.models.Expense;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class ExpenseCursorWrapper extends CursorWrapper {

    public ExpenseCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public static boolean isUUIDValid(String uuid){
        if( uuid == null) return false;
        try {
            // we have to convert to object and back to string because the built in fromString does not have
            // good validation logic.
            UUID fromStringUUID = UUID.fromString(uuid);
            String toStringUUID = fromStringUUID.toString();
            return toStringUUID.equals(uuid);
        } catch(IllegalArgumentException e) {
            return false;
        }
    }

    public static Calendar stringToCalendar(String stringDate)
    {
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(formatter.parse(stringDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        */
        return calendar;
    }

    public Expense getExpense() {
        String id = getString(getColumnIndex(DbSchema.ExpenseTable.Cols.EXPENSE_ID));
        String trip_id = getString(getColumnIndex(DbSchema.ExpenseTable.Cols.EXPENSE_TRIP_ID));
        String client_id = getString(getColumnIndex(DbSchema.ExpenseTable.Cols.EXPENSE_CLIENT_ID));
        String type = getString(getColumnIndex(DbSchema.ExpenseTable.Cols.EXPENSE_TYPE));
        String amount = getString(getColumnIndex(DbSchema.ExpenseTable.Cols.EXPENSE_AMOUNT));
        String date_from = getString(getColumnIndex(DbSchema.ExpenseTable.Cols.EXPENSE_DATE_FROM));
        String date_to = getString(getColumnIndex(DbSchema.ExpenseTable.Cols.EXPENSE_DATE_TO));
        String description = getString(getColumnIndex(DbSchema.ExpenseTable.Cols.EXPENSE_DESCRIPTION));
        String imgFile = getString(getColumnIndex(DbSchema.ExpenseTable.Cols.EXPENSE_IMAGE_FILE));

        Expense expense = new Expense(Integer.valueOf(id));
       // Expense expense = new Expense();
        if(isUUIDValid(client_id))
        expense.setTrip_id(Integer.valueOf(trip_id));
        expense.setClient_id(UUID.fromString(client_id));
        expense.setType(type);
        expense.setAmount(amount);
        expense.setDate_from(stringToCalendar(date_from));
        expense.setDate_to(stringToCalendar(date_to));
        expense.setDescription(description);
        expense.setImageFile(imgFile);

        return expense;

    }
}

