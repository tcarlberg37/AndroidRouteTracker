package com.example.mobileappsproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.Date;

public class RouteDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Routes.db";
    // 1 Routes db with 2 tables: Routes and Points

    public RouteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RouteContract.RouteEntity.SQL_CREATE);
        db.execSQL(PointsContract.PointsEntity.SQL_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("upgrading db", "upgrading db");
        db.execSQL(RouteContract.RouteEntity.SQL_DROP);
        db.execSQL(PointsContract.PointsEntity.SQL_DROP);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    // ROUTE table functions
    public long insertRoute(String route_name, double rating, String tags, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RouteContract.RouteEntity.COLUMN_ROUTE_NAME, route_name);
        values.put(RouteContract.RouteEntity.COLUMN_RATING, rating);
        values.put(RouteContract.RouteEntity.COLUMN_TAGS, tags);
        values.put(RouteContract.RouteEntity.COLUMN_DATE, date);

        return db.insert(RouteContract.RouteEntity.TABLE_NAME, null, values);
    }

    public Cursor getRoutes(String route_name){
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {BaseColumns._ID, RouteContract.RouteEntity.COLUMN_ROUTE_NAME,
                RouteContract.RouteEntity.COLUMN_RATING, RouteContract.RouteEntity.COLUMN_TAGS,
                RouteContract.RouteEntity.COLUMN_DATE
        };

        String sortOrder = RouteContract.RouteEntity._ID + " ASC"; // sort by ID

        if (route_name.equals("")) { // there is no route_name given, return all Routes (no selection or selectionArgs)
            return db.query(RouteContract.RouteEntity.TABLE_NAME, projection, null, null,null, null, sortOrder);
        } else {
            // Find Route with parameter route_name
            String selection = RouteContract.RouteEntity.COLUMN_ROUTE_NAME + " = ?";
            String[] selectionArgs = { route_name };
            return db.query(RouteContract.RouteEntity.TABLE_NAME, projection, selection, selectionArgs,null, null, sortOrder);
        }
    }

    public int deleteRoute(String route_name){
        SQLiteDatabase db = getWritableDatabase();
        String selection = RouteContract.RouteEntity.COLUMN_ROUTE_NAME + " LIKE ?";
        String[] selectionArgs = { route_name };
        // returns number of rows that were deleted
        return db.delete(RouteContract.RouteEntity.TABLE_NAME, selection, selectionArgs);
    }

    public int updateRoute(String old_route_name, String new_route_name, double rating, String tags, String date) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RouteContract.RouteEntity.COLUMN_ROUTE_NAME, new_route_name);
        values.put(RouteContract.RouteEntity.COLUMN_RATING, rating);
        values.put(RouteContract.RouteEntity.COLUMN_TAGS, tags);
        values.put(RouteContract.RouteEntity.COLUMN_DATE, date);

        String selection = RouteContract.RouteEntity.COLUMN_ROUTE_NAME + " LIKE ?";
        String[] selectionArgs = { old_route_name };
        // returns number of rows updated (should be just 1)
        return db.update(RouteContract.RouteEntity.TABLE_NAME, values, selection, selectionArgs);
    }

    public Cursor getID(String route_name) {
        SQLiteDatabase db = getReadableDatabase();

        // just get ID of the route
        String[] projection = {BaseColumns._ID};

        // Find Route with parameter route_name
        String selection = RouteContract.RouteEntity.COLUMN_ROUTE_NAME + " = ?";
        String[] selectionArgs = { route_name };
        return db.query(RouteContract.RouteEntity.TABLE_NAME, projection, selection, selectionArgs,null, null, null);
    }

    // POINTS table functions
    public long insertPoint(int route_id, long timestamp, double latitude, double longitude) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PointsContract.PointsEntity.COLUMN_ROUTE_ID, route_id);
        values.put(PointsContract.PointsEntity.COLUMN_TIMESTAMP, timestamp);
        values.put(PointsContract.PointsEntity.COLUMN_LATITUDE, latitude);
        values.put(PointsContract.PointsEntity.COLUMN_LONGITUDE, longitude);

        return db.insert(PointsContract.PointsEntity.TABLE_NAME, null, values);
    }

    public Cursor getPoints(long route_id){
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {BaseColumns._ID, PointsContract.PointsEntity.COLUMN_ROUTE_ID,
                PointsContract.PointsEntity.COLUMN_TIMESTAMP, PointsContract.PointsEntity.COLUMN_LATITUDE,
                PointsContract.PointsEntity.COLUMN_LONGITUDE
        };

        String sortOrder = PointsContract.PointsEntity._ID + " ASC"; // sort by ID ascending

        // Find all Points with COLUMN_ROUTE_ID == route_id
        String selection = PointsContract.PointsEntity.COLUMN_ROUTE_ID + " = ?";
        String[] selectionArgs = { route_id + "" };
        Cursor cursor = db.query(PointsContract.PointsEntity.TABLE_NAME, projection, selection, selectionArgs,null, null, sortOrder);
        return cursor;
    }

    public int deletePoints(long route_id){
        SQLiteDatabase db = getWritableDatabase();
        String selection = PointsContract.PointsEntity.COLUMN_ROUTE_ID + " = ?";
        String[] selectionArgs = { route_id + "" };
        // delete ALL the rows of Points associated with this route_id
        return db.delete(PointsContract.PointsEntity.TABLE_NAME, selection, selectionArgs);
    }
}