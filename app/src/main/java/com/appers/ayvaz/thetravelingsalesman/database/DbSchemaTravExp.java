package com.appers.ayvaz.thetravelingsalesman.database;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DbSchemaTravExp {
    //An expense has trip id a trip can have many expenses but an expense only 1 trip
    public static final class ExpenseTable {
        public static final String NAME = "expenses";
        public static final class Cols {
            public static final String EXPENSE_ID ="expense_id";             //Primary Key
            public static final String EXPENSE_TRIP_ID = "expense_trip_id";  //Foreign Key
            public static final String EXPENSE_CLIENT_ID ="client_id";       //Foreign Key
            public static final String EXPENSE_TYPE = "expense_type";
            public static final String EXPENSE_AMOUNT = "expense_amount";
            public static final String EXPENSE_DATE_FROM = "expense_date_from";
            public static final String EXPENSE_DATE_TO = "expense_date_to";
            public static final String EXPENSE_DESCRIPTION = "expense_description";
            public static final String EXPENSE_IMAGE = "image";

        }
    }
    //A trip does not include expense id since a trip can have many expenses
    public static final class TripTable {
        public static final String NAME = "tasks";
        public static final class Cols {
            public static final String TRIP_ID ="trip_id";                  //Primary Key
            public static final String TRIP_CLIENT_ID ="client_id";         //Foreign Key
            public static final String TRIP_TYPE = "trip_type";
            public static final String TRIP_FROM = "trip_from";
            public static final String TRIP_TO = "trip_to";
            public static final String TRIP_DATE_FROM = "trip_date_from";
            public static final String TRIP_DATE_TO = "trip_date_to";
            public static final String TRIP_DESCRIPTION = "trip_description";
            public static final String TRIP_IMAGE = "image";
        }
    }


}