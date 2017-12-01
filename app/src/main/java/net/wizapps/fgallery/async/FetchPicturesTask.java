package net.wizapps.fgallery.async;

import android.content.Context;
import android.os.AsyncTask;

import net.wizapps.fgallery.model.GalleryItem;
import net.wizapps.fgallery.tool.Events;
import net.wizapps.fgallery.tool.FlickrFetch;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class FetchPicturesTask extends AsyncTask<Context, Void, List<GalleryItem>> {

    private final int mPageNumber;

    public FetchPicturesTask(int pageNumber) {
        mPageNumber = pageNumber;
    }

    @Override
    protected void onPreExecute() {
        EventBus.getDefault().post(new Events.FetchStartEvent());
    }

    @Override
    protected List<GalleryItem> doInBackground(Context... contexts) {
        return new FlickrFetch().downloadGallery(contexts[0], mPageNumber);
    }

    @Override
    protected void onPostExecute(List<GalleryItem> pictures) {
        EventBus.getDefault().post(new Events.FetchFinishEvent(pictures));
    }
}