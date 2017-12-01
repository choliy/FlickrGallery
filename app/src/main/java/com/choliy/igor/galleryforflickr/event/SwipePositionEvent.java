package com.choliy.igor.galleryforflickr.event;

public class SwipePositionEvent {

    private int mPosition;

    public SwipePositionEvent(int position) {
        setPosition(position);
    }

    public int getPosition() {
        return mPosition;
    }

    private void setPosition(int position) {
        mPosition = position;
    }
}