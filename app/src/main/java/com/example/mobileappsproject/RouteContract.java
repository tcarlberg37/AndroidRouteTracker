package com.example.mobileappsproject;

import android.provider.BaseColumns;

public class RouteContract {
    private RouteContract() {} // Singleton instance

    public static class RouteEntity implements BaseColumns {
        public static final String TABLE_NAME = "Routes";
        public static final String COLUMN_ROUTE_NAME = "Route_Name";
        public static final String COLUMN_RATING = "Rating";
        public static final String COLUMN_TAGS = "Tags";
        public static final String COLUMN_DATE = "Date";


        public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
                _ID + " INTEGER PRIMARY KEY, " + COLUMN_ROUTE_NAME + " TEXT, "
                + COLUMN_RATING + " NUMBER, " + COLUMN_TAGS + " TEXT, " + COLUMN_DATE + " TEXT )";

        public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
