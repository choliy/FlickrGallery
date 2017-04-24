package com.choliy.igor.flickrgallery;

public class GalleryItem {

    private String mId;
    private String mTitle;
    private String mUploadDate;
    private String mOwnerName;
    private String mPictureUrl;

    public GalleryItem() {

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

    public String getOwnerName() {
        return mOwnerName;
    }

    public void setOwnerName(String ownerName) {
        mOwnerName = ownerName;
    }

    public String getPictureUrl() {
        return mPictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        mPictureUrl = pictureUrl;
    }
}