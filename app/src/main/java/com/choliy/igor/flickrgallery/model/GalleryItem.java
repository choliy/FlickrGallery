package com.choliy.igor.flickrgallery.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class GalleryItem implements Parcelable {

    private String mId;
    private String mTitle;
    private String mUploadDate;
    private String mOwnerId;
    private String mOwnerName;
    private String mDescription;
    private String mSmallListPictureUrl;
    private String mListPictureUrl;
    private String mExtraSmallPictureUrl;
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
            String smallListPictureUrl,
            String listPictureUrl,
            String extraSmallPictureUrl,
            String smallPictureUrl,
            String mediumPictureUrl,
            String bigPictureUrl) {

        setId(id);
        setTitle(title);
        setUploadDate(uploadDate);
        setOwnerId(ownerId);
        setOwnerName(ownerName);
        setDescription(description);
        setSmallListPictureUrl(smallListPictureUrl);
        setListPictureUrl(listPictureUrl);
        setExtraSmallPictureUrl(extraSmallPictureUrl);
        setSmallPictureUrl(smallPictureUrl);
        setMediumPictureUrl(mediumPictureUrl);
        setBigPictureUrl(bigPictureUrl);
    }

    protected GalleryItem(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mUploadDate = in.readString();
        mOwnerId = in.readString();
        mOwnerName = in.readString();
        mDescription = in.readString();
        mSmallListPictureUrl = in.readString();
        mListPictureUrl = in.readString();
        mExtraSmallPictureUrl = in.readString();
        mSmallPictureUrl = in.readString();
        mMediumPictureUrl = in.readString();
        mBigPictureUrl = in.readString();
    }

    public static final Creator<GalleryItem> CREATOR = new Creator<GalleryItem>() {
        @Override
        public GalleryItem createFromParcel(Parcel in) {
            return new GalleryItem(in);
        }

        @Override
        public GalleryItem[] newArray(int size) {
            return new GalleryItem[size];
        }
    };

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

    public String getSmallListPictureUrl() {
        return mSmallListPictureUrl;
    }

    public void setSmallListPictureUrl(String smallListPictureUrl) {
        mSmallListPictureUrl = smallListPictureUrl;
    }

    public String getListPictureUrl() {
        return mListPictureUrl;
    }

    public void setListPictureUrl(String listPictureUrl) {
        mListPictureUrl = listPictureUrl;
    }

    public String getExtraSmallPictureUrl() {
        return mExtraSmallPictureUrl;
    }

    public void setExtraSmallPictureUrl(String extraSmallPictureUrl) {
        mExtraSmallPictureUrl = extraSmallPictureUrl;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mUploadDate);
        parcel.writeString(mOwnerId);
        parcel.writeString(mOwnerName);
        parcel.writeString(mDescription);
        parcel.writeString(mSmallListPictureUrl);
        parcel.writeString(mListPictureUrl);
        parcel.writeString(mExtraSmallPictureUrl);
        parcel.writeString(mSmallPictureUrl);
        parcel.writeString(mMediumPictureUrl);
        parcel.writeString(mBigPictureUrl);
    }
}