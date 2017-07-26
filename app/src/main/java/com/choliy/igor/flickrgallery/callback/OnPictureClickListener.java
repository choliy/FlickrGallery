package com.choliy.igor.flickrgallery.callback;

import com.choliy.igor.flickrgallery.model.GalleryItem;

public interface OnPictureClickListener {

    void onRequestedLastItem(int position);

    void onPictureClicked(GalleryItem item);

}