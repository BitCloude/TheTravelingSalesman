package com.simbiosyscorp.thetravelingsalesman.database;

/**
 * Created by D on 027 12/27.
 */
public class DbSchema {
    public static final class ClientTable {
        public static final String NAME = "clients";
        public static final class Cols {
            public static final String _ID = "_id";
            public static final String UUID = "uuid";
            public static final String FIRST_NAME = "first_name";
            public static final String LAST_NAME = "last_name";
            public static final String FIRST_PHONE = "first_phone";
            public static final String SECOND_PHONE = "second_phone";
            public static final String EMAIL = "email";
            public static final String COMPANY = "company";
            public static final String ADDRESS = "address";
            public static final String NOTE = "note";
            public static final String STARED = "stared";
            public static final String LINKEDIN = "linkedin";
            public static final String CONTACT_ID = "contact_id";
//            public static final String IMAGE = "img";

        }
    }

    public static final class TaskTable {
        public static final String NAME = "tasks";
        public static final class Cols {
            public static final String _ID = "_id";
            public static final String EVENT_ID = "event_id";
            public static final String CLIENT_ID = "client_id";
        }
    }


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
            public static final String EXPENSE_IMAGE_FILE = "image_file";

        }
    }

    //A trip does not include expense id since a trip can have many expenses
    public static final class TripTable {
        public static final String NAME = "trip";
        public static final class Cols {
            public static final String TRIP_ID ="trip_id";                  //Primary Key
            public static final String TRIP_CLIENT_ID ="client_id";         //Foreign Key
            public static final String TRIP_TYPE = "trip_type";
            public static final String TRIP_FROM = "trip_from";
            public static final String TRIP_TO = "trip_to";
            public static final String TRIP_BOARDING = "trip_boarding_pass";
            public static final String TRIP_DATE_FROM = "trip_date_from";
            public static final String TRIP_DATE_TO = "trip_date_to";
            public static final String TRIP_DESCRIPTION = "trip_description";
            public static final String TRIP_IMAGE_FILE = "image_file";
        }
    }
}
