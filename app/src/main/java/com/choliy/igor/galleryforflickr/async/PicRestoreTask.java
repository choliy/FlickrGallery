package com.choliy.igor.galleryforflickr.async;

import android.content.Context;
import android.os.AsyncTask;

import com.choliy.igor.galleryforflickr.data.FlickrLab;
import com.choliy.igor.galleryforflickr.event.RestoreFinishEvent;
import com.choliy.igor.galleryforflickr.event.RestoreStartEvent;
import com.choliy.igor.galleryforflickr.model.GalleryItem;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class PicRestoreTask extends AsyncTask<Context, Void, Void> {

    private List<GalleryItem> mRestoreItems;

    public PicRestoreTask(List<GalleryItem> restoreItems) {
        mRestoreItems = restoreItems;
    }

    @Override
    protected void onPreExecute() {
        EventBus.getDefault().post(new RestoreStartEvent());
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        FlickrLab.getInstance(contexts[0]).restoreAllPictures(mRestoreItems);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        EventBus.getDefault().post(new RestoreFinishEvent());
    }
}