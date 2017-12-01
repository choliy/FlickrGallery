package com.choliy.igor.galleryforflickr.event;

import com.choliy.igor.galleryforflickr.model.GalleryItem;

import java.util.List;

public class FetchFinishEvent {

    private List<GalleryItem> mPictures;

    public FetchFinishEvent(List<GalleryItem> pictures) {
        setPictures(pictures);
    }

    public List<GalleryItem> getPictures() {
        return mPictures;
    }

    private void setPictures(List<GalleryItem> pictures) {
        mPictures = pictures;
    }
}