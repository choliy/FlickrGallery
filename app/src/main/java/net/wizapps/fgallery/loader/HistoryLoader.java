package net.wizapps.fgallery.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import net.wizapps.fgallery.data.FlickrLab;
import net.wizapps.fgallery.model.HistoryItem;

import java.util.List;

public class HistoryLoader extends AsyncTaskLoader<List<HistoryItem>> {

    public static final int HISTORY_LOADER_ID = 111;
    private List<HistoryItem> mHistoryItems;

    public HistoryLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (mHistoryItems == null) {
            forceLoad();
        } else {
            deliverResult(mHistoryItems);
        }
    }

    @Override
    public List<HistoryItem> loadInBackground() {
        return FlickrLab.getInstance(getContext()).getHistory();
    }

    @Override
    public void deliverResult(List<HistoryItem> historyItems) {
        mHistoryItems = historyItems;
        super.deliverResult(historyItems);
    }
}