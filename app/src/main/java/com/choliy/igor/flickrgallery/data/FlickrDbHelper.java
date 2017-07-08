package com.choliy.igor.flickrgallery.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class FlickrDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "flickrBase.db";
    private static final int DATABASE_VERSION = 1;

    FlickrDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + FlickrContract.TABLE_HISTORY + " (" +
                FlickrContract._ID + " INTEGER PRIMARY KEY, " +
                FlickrContract.COLUMN_HISTORY_TITLE + " TEXT NOT NULL, " +
                FlickrContract.COLUMN_HISTORY_DATE + " TEXT NOT NULL, " +
                FlickrContract.COLUMN_HISTORY_TIME + " TEXT NOT NULL);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FlickrContract.TABLE_HISTORY);
        onCreate(db);
    }
}