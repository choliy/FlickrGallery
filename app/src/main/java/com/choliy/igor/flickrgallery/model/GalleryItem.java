package com.choliy.igor.flickrgallery.model;

import android.net.Uri;

public class GalleryItem {

    private String mId;
    private String mTitle;
    private String mUploadDate;
    private String mOwnerId;
    private String mOwnerName;
    private String mDescription;
    private String mListPictureUrl;
    private String mSmallPictureUrl;
    private String mMediumPictureUrl;
    private String mBigPictureUrl;

    public GalleryItem() {
    }

    public GalleryItem(
            String id,
            String title,
            String uploadDate,
            String ownerId,
            String ownerName,
            String description,
            String listPictureUrl,
            String smallPictureUrl,
            String mediumPictureUrl,
            String bigPictureUrl) {

        setId(id);
        setTitle(title);
        setUploadDate(uploadDate);
        setOwnerId(ownerId);
        setOwnerName(ownerName);
        setDescription(description);
        setListPictureUrl(listPictureUrl);
        setSmallPictureUrl(smallPictureUrl);
        setMediumPictureUrl(mediumPictureUrl);
        setBigPictureUrl(bigPictureUrl);
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getListPictureUrl() {
        return mListPictureUrl;
    }

    public void setListPictureUrl(String listPictureUrl) {
        mListPictureUrl = listPictureUrl;
    }

    public String getSmallPictureUrl() {
        return mSmallPictureUrl;
    }

    public void setSmallPictureUrl(String smallPictureUrl) {
        mSmallPictureUrl = smallPictureUrl;
    }

    public String getMediumPictureUrl() {
        return mMediumPictureUrl;
    }

    public void setMediumPictureUrl(String mediumPictureUrl) {
        mMediumPictureUrl = mediumPictureUrl;
    }

    public String getBigPictureUrl() {
        return mBigPictureUrl;
    }

    public void setBigPictureUrl(String bigPictureUrl) {
        mBigPictureUrl = bigPictureUrl;
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