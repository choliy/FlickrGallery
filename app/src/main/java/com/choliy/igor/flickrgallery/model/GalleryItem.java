package com.choliy.igor.flickrgallery.model;

import android.net.Uri;

public class GalleryItem {

    private String mId;
    private String mTitle;
    private String mUploadDate;
    private String mOwnerId;
    private String mOwnerName;
    private String mSmallPictureUrl;

    public GalleryItem() {
    }

    public GalleryItem(
            String id,
            String title,
            String uploadDate,
            String ownerId,
            String ownerName,
            String smallPictureUrl) {

        mId = id;
        mTitle = title;
        mUploadDate = uploadDate;
        mOwnerId = ownerId;
        mOwnerName = ownerName;
        mSmallPictureUrl = smallPictureUrl;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUploadDate() {
        return mUploadDate;
    }

    public void setUploadDate(String uploadDate) {
        mUploadDate = uploadDate;
    }

    public String getOwnerId() {
        return mOwnerId;
    }

    public void setOwnerId(String ownerId) {
        mOwnerId = ownerId;
    }

    public String getOwnerName() {
        return mOwnerName;
    }

    public void setOwnerName(String ownerName) {
        mOwnerName = ownerName;
    }

    public String getSmallPictureUrl() {
        return mSmallPictureUrl;
    }

    public void setSmallPictureUrl(String smallPictureUrl) {
        mSmallPictureUrl = smallPictureUrl;
    }

    public String getItemUri() {
        Uri uri = Uri.parse("http://www.flickr.com/photos/")
                .buildUpon()
                .appendPath(mOwnerId)
                .appendPath(mId)
                .build();

        return uri.toString();
    }
}