package com.choliy.igor.galleryforflickr.event;

public class HistoryTitleEvent {

    private String mHistoryTitle;

    public HistoryTitleEvent(String historyTitle) {
        setHistoryTitle(historyTitle);
    }

    public String getHistoryTitle() {
        return mHistoryTitle;
    }

    private void setHistoryTitle(String historyTitle) {
        mHistoryTitle = historyTitle;
    }
}