package com.example.mobileappsproject;

import android.provider.BaseColumns;

public class PointsContract {
    private PointsContract() {} // Singleton instance

    public static class PointsEntity implements BaseColumns {
        public static final String TABLE_NAME = "Points";
        public static final String COLUMN_ROUTE_ID = "Route_Id";
        public static final String COLUMN_TIMESTAMP = "Timestamp";
        public static final String COLUMN_LATITUDE = "Latitude";
        public static final String COLUMN_LONGITUDE = "Longitude";

        // COLUMN_ROUTE_ID has a foreign key reference to a Route ID in the Routes table
        public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
                _ID + " INTEGER PRIMARY KEY, " + COLUMN_ROUTE_ID + " INTEGER, " + COLUMN_TIMESTAMP + " NUMBER, "
                + COLUMN_LATITUDE + " NUMBER, " + COLUMN_LONGITUDE + " NUMBER, FOREIGN KEY (" + COLUMN_ROUTE_ID +
                ") REFERENCES " + RouteContract.RouteEntity.TABLE_NAME + "(" + RouteContract.RouteEntity._ID + ") );";

        public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
