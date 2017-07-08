package com.choliy.igor.flickrgallery.data;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.choliy.igor.flickrgallery.model.HistoryItem;

class HistoryCursorWrapper extends CursorWrapper {

    HistoryCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    HistoryItem getHistoryItem() {
        String id = getString(getColumnIndex(FlickrContract._ID));
        String historyTitle = getString(getColumnIndex(FlickrContract.COLUMN_HISTORY_TITLE));
        String historyDate = getString(getColumnIndex(FlickrContract.COLUMN_HISTORY_DATE));
        String historyTime = getString(getColumnIndex(FlickrContract.COLUMN_HISTORY_TIME));

        return new HistoryItem(id, historyTitle, historyDate, historyTime);
    }
}