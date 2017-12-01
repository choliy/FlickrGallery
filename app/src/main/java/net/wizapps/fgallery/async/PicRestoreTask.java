package net.wizapps.fgallery.async;

import android.content.Context;
import android.os.AsyncTask;

import net.wizapps.fgallery.data.FlickrLab;
import net.wizapps.fgallery.model.GalleryItem;
import net.wizapps.fgallery.tool.Events;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class PicRestoreTask extends AsyncTask<Context, Void, Void> {

    private final List<GalleryItem> mRestoreItems;

    public PicRestoreTask(List<GalleryItem> restoreItems) {
        mRestoreItems = restoreItems;
    }

    @Override
    protected void onPreExecute() {
        EventBus.getDefault().post(new Events.RestoreStartEvent());
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        FlickrLab.getInstance(contexts[0]).restoreAllPictures(mRestoreItems);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        EventBus.getDefault().post(new Events.RestoreFinishEvent());
    }
}