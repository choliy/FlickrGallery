package com.choliy.igor.galleryforflickr.event;

public class HistoryStartEvent {

    private boolean mIsStartClicked;

    public HistoryStartEvent(boolean isStartClicked) {
        setStartClicked(isStartClicked);
    }

    public boolean isStartClicked() {
        return mIsStartClicked;
    }

    private void setStartClicked(boolean startClicked) {
        mIsStartClicked = startClicked;
    }
}