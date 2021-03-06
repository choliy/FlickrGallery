package net.wizapps.fgallery.async;

import android.content.Context;
import android.os.AsyncTask;

import net.wizapps.fgallery.data.FlickrLab;
import net.wizapps.fgallery.model.HistoryItem;
import net.wizapps.fgallery.tool.Events;
import net.wizapps.fgallery.util.PrefUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class SaveHistoryTask extends AsyncTask<Context, Void, Void> {

    private final boolean mClearHistoryBase;
    private final List<HistoryItem> mSavedHistory;

    public SaveHistoryTask(boolean clearHistoryBase, List<HistoryItem> savedHistory) {
        mClearHistoryBase = clearHistoryBase;
        mSavedHistory = savedHistory;
    }

    @Override
    protected void onPreExecute() {
        EventBus.getDefault().post(new Events.SaveStartEvent());
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
        EventBus.getDefault().post(new Events.SaveFinishEvent(mClearHistoryBase));
    }
}