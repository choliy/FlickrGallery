package com.choliy.igor.flickrgallery.event;

public class TopListEvent {

    private int mScrollPosition;

    public TopListEvent(int scrollPosition) {
        setScrollPosition(scrollPosition);
    }

    public int getScrollPosition() {
        return mScrollPosition;
    }

    private void setScrollPosition(int scrollPosition) {
        mScrollPosition = scrollPosition;
    }
}