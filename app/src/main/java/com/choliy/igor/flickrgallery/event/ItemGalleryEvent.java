package com.choliy.igor.flickrgallery.event;

import com.choliy.igor.flickrgallery.model.GalleryItem;

public class ItemGalleryEvent {

    private GalleryItem mItem;

    public ItemGalleryEvent(GalleryItem item) {
        setItem(item);
    }

    public GalleryItem getItem() {
        return mItem;
    }

    private void setItem(GalleryItem item) {
        mItem = item;
    }
}