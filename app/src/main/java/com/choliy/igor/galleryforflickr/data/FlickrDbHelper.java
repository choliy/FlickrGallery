package com.choliy.igor.galleryforflickr.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class FlickrDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "flickrGalleryBase.db";
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

        db.execSQL(createPicTable(FlickrContract.TABLE_SAVED));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(updateTable(FlickrContract.TABLE_HISTORY));
        db.execSQL(updateTable(FlickrContract.TABLE_SAVED));
        onCreate(db);
    }

    private String createPicTable(String tableName) {
        return "CREATE TABLE " + tableName + " (" +
                FlickrContract._ID + " INTEGER PRIMARY KEY, " +
                FlickrContract.COLUMN_PICTURE_USER_ID + " TEXT NOT NULL, " +
                FlickrContract.COLUMN_PICTURE_TITLE + " TEXT NOT NULL, " +
                FlickrContract.COLUMN_PICTURE_DATE + " TEXT NOT NULL, " +
                FlickrContract.COLUMN_PICTURE_OWNER_ID + " TEXT NOT NULL, " +
                FlickrContract.COLUMN_PICTURE_OWNER_NAME + " TEXT NOT NULL, " +
                FlickrContract.COLUMN_PICTURE_DESCRIPTION + " TEXT NOT NULL, " +
                FlickrContract.COLUMN_PICTURE_SMALL_LIST_URL + " TEXT NOT NULL, " +
                FlickrContract.COLUMN_PICTURE_LIST_URL + " TEXT NOT NULL, " +
                FlickrContract.COLUMN_PICTURE_EXTRA_SMALL_URL + " TEXT NOT NULL, " +
                FlickrContract.COLUMN_PICTURE_SMALL_URL + " TEXT NOT NULL, " +
                FlickrContract.COLUMN_PICTURE_MEDIUM_URL + " TEXT NOT NULL, " +
                FlickrContract.COLUMN_PICTURE_BIG_URL + " TEXT NOT NULL);";
    }

    private String updateTable(String tableName) {
        return "DROP TABLE IF EXISTS " + tableName;
    }
}