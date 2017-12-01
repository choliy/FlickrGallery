package net.wizapps.fgallery.data;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.wizapps.fgallery.model.HistoryItem;

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