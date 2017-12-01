package com.choliy.igor.galleryforflickr.async;

import android.content.Context;
import android.os.AsyncTask;

import com.choliy.igor.galleryforflickr.data.FlickrLab;
import com.choliy.igor.galleryforflickr.event.SaveFinishEvent;
import com.choliy.igor.galleryforflickr.event.SaveStartEvent;
import com.choliy.igor.galleryforflickr.model.HistoryItem;
import com.choliy.igor.galleryforflickr.util.PrefUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class SaveHistoryTask extends AsyncTask<Context, Void, Void> {

    private boolean mClearHistoryBase;
    private List<HistoryItem> mSavedHistory;

    public SaveHistoryTask(boolean clearHistoryBase, List<HistoryItem> savedHistory) {
        mClearHistoryBase = clearHistoryBase;
        mSavedHistory = savedHistory;
    }

    @Override
    protected void onPreExecute() {
        EventBus.getDefault().post(new SaveStartEvent());
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        if (mClearHistoryBase) {
            FlickrLab.getInstance(contexts[0]).clearAllHistory();
            PrefUtils.setStoredQuery(contexts[0], null);
        } else
            FlickrLab.getInstance(contexts[0]).restoreHistory(mSavedHistory);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        EventBus.getDefault().post(new SaveFinishEvent(mClearHistoryBase));
    }
}