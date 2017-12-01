package com.choliy.igor.galleryforflickr.event;

public class PictureClickEvent {

    private String mPicUrl;
    private byte[] mBytes;

    public PictureClickEvent(String picUrl, byte[] bytes) {
        setPicUrl(picUrl);
        setBytes(bytes);
    }

    public String getPicUrl() {
        return mPicUrl;
    }

    private void setPicUrl(String picUrl) {
        mPicUrl = picUrl;
    }

    public byte[] getBytes() {
        return mBytes;
    }

    private void setBytes(byte[] bytes) {
        mBytes = bytes;
    }
}