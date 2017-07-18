package com.choliy.igor.flickrgallery.async;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.choliy.igor.flickrgallery.data.FlickrLab;
import com.choliy.igor.flickrgallery.model.HistoryItem;

import java.util.List;

public class HistoryLoader extends AsyncTaskLoader<List<HistoryItem>> {

    public static final int HISTORY_LOADER_ID = 333;
    private Context mContext;
    private List<HistoryItem> mHistoryItems;

    public HistoryLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        if (mHistoryItems == null) forceLoad();
        else deliverResult(mHistoryItems);
    }

    @Override
    public List<HistoryItem> loadInBackground() {
        return FlickrLab.getInstance(mContext).getHistory();
    }

    @Override
    public void deliverResult(List<HistoryItem> historyItems) {
        mHistoryItems = historyItems;
        super.deliverResult(historyItems);
    }
}