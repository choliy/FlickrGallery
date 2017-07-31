package com.choliy.igor.flickrgallery.event;

public class ItemLastEvent {

    private int mPosition;

    public ItemLastEvent(int position) {
        setPosition(position);
    }

    public int getPosition() {
        return mPosition;
    }

    private void setPosition(int position) {
        mPosition = position;
    }
}