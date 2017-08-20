package com.choliy.igor.galleryforflickr.event;

public class ItemPositionEvent {

    private int mPosition;

    public ItemPositionEvent(int position) {
        setPosition(position);
    }

    public int getPosition() {
        return mPosition;
    }

    private void setPosition(int position) {
        mPosition = position;
    }
}