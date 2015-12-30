package com.appers.ayvaz.thetravelingsalesman.database;

/**
 * Created by D on 027 12/27.
 */
public class DbSchema {
    public static final class ClientTable {
        public static final String NAME = "clients";
        public static final class Cols {
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

        }
    }
}
