package com.choliy.igor.galleryforflickr.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.choliy.igor.galleryforflickr.FlickrConstants;

public class GalleryItem implements Parcelable {

    private String mDbId;
    private String mUserId;
    private String mTitle;
    private String mDate;
    private String mOwnerId;
    private String mOwnerName;
    private String mDescription;
    private String mSmallListPicUrl;
    private String mListPicUrl;
    private String mExtraSmallPicUrl;
    private String mSmallPicUrl;
    private String mMediumPicUrl;
    private String mBigPicUrl;

    public GalleryItem() {
    }

    private GalleryItem(Parcel in) {
        mDbId = in.readString();
        mUserId = in.readString();
        mTitle = in.readString();
        mDate = in.readString();
        mOwnerId = in.readString();
        mOwnerName = in.readString();
        mDescription = in.readString();
        mSmallListPicUrl = in.readString();
        mListPicUrl = in.readString();
        mExtraSmallPicUrl = in.readString();
        mSmallPicUrl = in.readString();
        mMediumPicUrl = in.readString();
        mBigPicUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mDbId);
        parcel.writeString(mUserId);
        parcel.writeString(mTitle);
        parcel.writeString(mDate);
        parcel.writeString(mOwnerId);
        parcel.writeString(mOwnerName);
        parcel.writeString(mDescription);
        parcel.writeString(mSmallListPicUrl);
        parcel.writeString(mListPicUrl);
        parcel.writeString(mExtraSmallPicUrl);
        parcel.writeString(mSmallPicUrl);
        parcel.writeString(mMediumPicUrl);
        parcel.writeString(mBigPicUrl);
    }

    @Override
    public int describeContents() {
        return FlickrConstants.INT_ZERO;
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

    public String getDbId() {
        return mDbId;
    }

    public void setDbId(String dbId) {
        mDbId = dbId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
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

    public String getSmallListPicUrl() {
        return mSmallListPicUrl;
    }

    public void setSmallListPicUrl(String smallListPicUrl) {
        mSmallListPicUrl = smallListPicUrl;
    }

    public String getListPicUrl() {
        return mListPicUrl;
    }

    public void setListPicUrl(String listPicUrl) {
        mListPicUrl = listPicUrl;
    }

    public String getExtraSmallPicUrl() {
        return mExtraSmallPicUrl;
    }

    public void setExtraSmallPicUrl(String extraSmallPicUrl) {
        mExtraSmallPicUrl = extraSmallPicUrl;
    }

    public String getSmallPicUrl() {
        return mSmallPicUrl;
    }

    public void setSmallPicUrl(String smallPicUrl) {
        mSmallPicUrl = smallPicUrl;
    }

    public String getMediumPicUrl() {
        return mMediumPicUrl;
    }

    public void setMediumPicUrl(String mediumPicUrl) {
        mMediumPicUrl = mediumPicUrl;
    }

    public String getBigPicUrl() {
        return mBigPicUrl;
    }

    public void setBigPicUrl(String bigPicUrl) {
        mBigPicUrl = bigPicUrl;
    }

    public String getItemUri() {
        Uri uri = Uri.parse("http://www.flickr.com/photos/")
                .buildUpon()
                .appendPath(getOwnerId())
                .appendPath(getUserId())
                .build();

        return uri.toString();
    }
}