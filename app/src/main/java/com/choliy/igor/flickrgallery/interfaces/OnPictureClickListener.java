package com.choliy.igor.flickrgallery.interfaces;

import com.choliy.igor.flickrgallery.model.GalleryItem;

public interface OnPictureClickListener {

    void onRequestedLastItem(int position);

    void onPictureClicked(GalleryItem item);

}