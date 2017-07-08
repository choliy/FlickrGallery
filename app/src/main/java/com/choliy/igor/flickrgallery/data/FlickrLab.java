package com.choliy.igor.flickrgallery.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public void addHistory(HistoryItem item, boolean restored) {
        ContentValues values = new ContentValues();
        if (restored) values.put(FlickrContract._ID, item.getId());
        values.put(FlickrContract.COLUMN_HISTORY_TITLE, item.getHistoryTitle());
        values.put(FlickrContract.COLUMN_HISTORY_DATE, item.getHistoryDate());
        values.put(FlickrContract.COLUMN_HISTORY_TIME, item.getHistoryTime());
        mDatabase.insert(FlickrContract.TABLE_HISTORY, null, values);
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

    public void deleteSingleHistory(String id) {
        mDatabase.delete(
                FlickrContract.TABLE_HISTORY,
                FlickrContract._ID + " = ?",
                new String[]{id});
    }

    public int deleteAllHistory() {
        return mDatabase.delete(FlickrContract.TABLE_HISTORY, null, null);
    }
}