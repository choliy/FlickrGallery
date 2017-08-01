package com.choliy.igor.flickrgallery.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.model.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class FlickrLab {

    private static FlickrLab sFlickrLab;
    private SQLiteDatabase mDatabase;

    private FlickrLab(Context context) {
        mDatabase = new FlickrDbHelper(context.getApplicationContext()).getWritableDatabase();
    }

    public static FlickrLab getInstance(Context context) {
        if (sFlickrLab == null) sFlickrLab = new FlickrLab(context);
        return sFlickrLab;
    }

    /**
     * History DB functions
     */
    public void addHistory(HistoryItem item, boolean restored) {
        ContentValues values = new ContentValues();
        if (restored) values.put(FlickrContract._ID, item.getId());
        values.put(FlickrContract.COLUMN_HISTORY_TITLE, item.getHistoryTitle());
        values.put(FlickrContract.COLUMN_HISTORY_DATE, item.getHistoryDate());
        values.put(FlickrContract.COLUMN_HISTORY_TIME, item.getHistoryTime());
        mDatabase.insert(FlickrContract.TABLE_HISTORY, null, values);
    }

    public List<HistoryItem> getHistory() {
        List<HistoryItem> history = new ArrayList<>();
        Cursor cursor = mDatabase.query(
                FlickrContract.TABLE_HISTORY,
                null, null, null, null, null,
                FlickrContract._ID + " DESC");

        if (cursor == null) return null;

        HistoryCursorWrapper cursorWrapper = new HistoryCursorWrapper(cursor);
        cursorWrapper.moveToFirst();
        try {
            while (!cursorWrapper.isAfterLast()) {
                history.add(cursorWrapper.getHistoryItem());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
            cursor.close();
        }

        return history;
    }

    public void restoreHistory(List<HistoryItem> history) {
        for (HistoryItem item : history) {
            ContentValues values = new ContentValues();
            values.put(FlickrContract._ID, item.getId());
            values.put(FlickrContract.COLUMN_HISTORY_TITLE, item.getHistoryTitle());
            values.put(FlickrContract.COLUMN_HISTORY_DATE, item.getHistoryDate());
            values.put(FlickrContract.COLUMN_HISTORY_TIME, item.getHistoryTime());
            mDatabase.insert(FlickrContract.TABLE_HISTORY, null, values);
        }
    }

    public void deleteHistory(String id) {
        mDatabase.delete(
                FlickrContract.TABLE_HISTORY,
                FlickrContract._ID + " = ?",
                new String[]{id});
    }

    public int clearAllHistory() {
        return mDatabase.delete(FlickrContract.TABLE_HISTORY, null, null);
    }

    /**
     * Saved pictures DB functions
     */
    public void addPicture(GalleryItem item) {
        mDatabase.insert(FlickrContract.TABLE_SAVED, null, getContentValue(item, Boolean.FALSE));
    }

    public void restorePicture(GalleryItem item) {
        mDatabase.insert(FlickrContract.TABLE_SAVED, null, getContentValue(item, Boolean.TRUE));
    }

    public List<GalleryItem> getSavedPictures() {
        List<GalleryItem> pictures = new ArrayList<>();
        Cursor cursor = mDatabase.query(
                FlickrContract.TABLE_SAVED,
                null, null, null, null, null,
                FlickrContract._ID + " DESC");

        if (cursor == null) return null;

        SavedPicCursorWrapper cursorWrapper = new SavedPicCursorWrapper(cursor);
        cursorWrapper.moveToFirst();
        try {
            while (!cursorWrapper.isAfterLast()) {
                pictures.add(cursorWrapper.getPictureItem());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
            cursor.close();
        }

        return pictures;
    }

    public void restoreAllPictures(List<GalleryItem> items) {
        for (GalleryItem item : items) {
            mDatabase.insert(FlickrContract.TABLE_SAVED, null, getContentValue(item, Boolean.TRUE));
        }
    }

    public void deletePicture(String id) {
        mDatabase.delete(
                FlickrContract.TABLE_SAVED,
                FlickrContract._ID + " = ?",
                new String[]{id});
    }

    public void deleteAllPictures() {
        mDatabase.delete(FlickrContract.TABLE_SAVED, null, null);
    }

    private ContentValues getContentValue(GalleryItem item, boolean restored) {
        ContentValues values = new ContentValues();
        if (restored) values.put(FlickrContract._ID, item.getDbId());
        values.put(FlickrContract.COLUMN_PICTURE_USER_ID, item.getUserId());
        values.put(FlickrContract.COLUMN_PICTURE_TITLE, item.getTitle());
        values.put(FlickrContract.COLUMN_PICTURE_DATE, item.getDate());
        values.put(FlickrContract.COLUMN_PICTURE_OWNER_ID, item.getOwnerId());
        values.put(FlickrContract.COLUMN_PICTURE_OWNER_NAME, item.getOwnerName());
        values.put(FlickrContract.COLUMN_PICTURE_DESCRIPTION, item.getDescription());
        values.put(FlickrContract.COLUMN_PICTURE_SMALL_LIST_URL, item.getSmallListPicUrl());
        values.put(FlickrContract.COLUMN_PICTURE_LIST_URL, item.getListPicUrl());
        values.put(FlickrContract.COLUMN_PICTURE_EXTRA_SMALL_URL, item.getExtraSmallPicUrl());
        values.put(FlickrContract.COLUMN_PICTURE_SMALL_URL, item.getSmallPicUrl());
        values.put(FlickrContract.COLUMN_PICTURE_MEDIUM_URL, item.getMediumPicUrl());
        values.put(FlickrContract.COLUMN_PICTURE_BIG_URL, item.getBigPicUrl());
        return values;
    }
}