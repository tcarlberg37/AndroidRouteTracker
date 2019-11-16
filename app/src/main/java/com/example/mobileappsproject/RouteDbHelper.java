package com.example.mobileappsproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.Date;

public class RouteDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Routes.db";

    public RouteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RouteContract.RouteEntity.SQL_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(RouteContract.RouteEntity.SQL_DROP);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insert(String route_name, Float rating, String tags, String date) {
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

        String sortOrder = RouteContract.RouteEntity._ID + " ASC"; // sort by ID ascending

        if (route_name.equals("")) { // there is no route_name given, return all Routes (no selection or selectionArgs)
            Cursor cursor = db.query(RouteContract.RouteEntity.TABLE_NAME, projection, null, null,null, null, sortOrder);
            return cursor;
        } else {
            // Find Route with parameter route_name
            String selection = RouteContract.RouteEntity.COLUMN_ROUTE_NAME + " = ?";
            String[] selectionArgs = { route_name };
            Cursor cursor = db.query(RouteContract.RouteEntity.TABLE_NAME, projection, selection, selectionArgs,null, null, sortOrder);
            return cursor;
        }
    }

    public int deleteRoute(String route_name){
        SQLiteDatabase db = getWritableDatabase();
        String selection = RouteContract.RouteEntity.COLUMN_ROUTE_NAME + " LIKE ?";
        String[] selectionArgs = { route_name };
        int deletedRows = db.delete(RouteContract.RouteEntity.TABLE_NAME, selection, selectionArgs);
        return deletedRows; // number of rows that were deleted
    }

    public int updateRoute(String old_route_name, String new_route_name, Float rating, String tags, String date) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RouteContract.RouteEntity.COLUMN_ROUTE_NAME, new_route_name);
        values.put(RouteContract.RouteEntity.COLUMN_RATING, rating);
        values.put(RouteContract.RouteEntity.COLUMN_TAGS, tags);
        values.put(RouteContract.RouteEntity.COLUMN_DATE, date);

        String selection = RouteContract.RouteEntity.COLUMN_ROUTE_NAME + " LIKE ?";
        String[] selectionArgs = { old_route_name };

        int count = db.update(RouteContract.RouteEntity.TABLE_NAME, values, selection, selectionArgs);
        return count; // number of rows updated (should be just 1)
    }
}