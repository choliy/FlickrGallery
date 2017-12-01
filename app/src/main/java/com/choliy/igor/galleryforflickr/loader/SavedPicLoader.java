package com.choliy.igor.galleryforflickr.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.choliy.igor.galleryforflickr.data.FlickrLab;
import com.choliy.igor.galleryforflickr.model.GalleryItem;

import java.util.List;

public class SavedPicLoader extends AsyncTaskLoader<List<GalleryItem>> {

    public static final int SAVED_PIC_LOADER_ID = 222;
    private List<GalleryItem> mGalleryItems;

    public SavedPicLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (mGalleryItems == null) {
            forceLoad();
        } else {
            deliverResult(mGalleryItems);
        }
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