package com.choliy.igor.flickrgallery.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.choliy.igor.flickrgallery.data.FlickrLab;
import com.choliy.igor.flickrgallery.model.GalleryItem;

import java.util.List;

public class SavedPicLoader extends AsyncTaskLoader<List<GalleryItem>> {

    public static final int SAVED_PIC_LOADER_ID = 555;
    private List<GalleryItem> mGalleryItems;

    public SavedPicLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (mGalleryItems == null) forceLoad();
        else deliverResult(mGalleryItems);
    }

    @Override
    public List<GalleryItem> loadInBackground() {
        return FlickrLab.getInstance(getContext()).getSavedPictures();
    }

    @Override
    public void deliverResult(List<GalleryItem> data) {
        mGalleryItems = data;
        super.deliverResult(data);
    }
}