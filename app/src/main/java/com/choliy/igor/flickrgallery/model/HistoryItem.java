package com.choliy.igor.flickrgallery.model;

public class HistoryItem {

    private String mId;
    private String mHistoryTitle;
    private String mHistoryDate;
    private String mHistoryTime;

    public HistoryItem(String historyTitle, String historyDate, String historyTime) {
        setHistoryTitle(historyTitle);
        setHistoryDate(historyDate);
        setHistoryTime(historyTime);
    }

    public HistoryItem(String id, String historyTitle, String historyDate, String historyTime) {
        this(historyTitle, historyDate, historyTime);
        setId(id);
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getHistoryTitle() {
        return mHistoryTitle;
    }

    private void setHistoryTitle(String historyTitle) {
        mHistoryTitle = historyTitle;
    }

    public String getHistoryDate() {
        return mHistoryDate;
    }

    private void setHistoryDate(String historyDate) {
        mHistoryDate = historyDate;
    }

    public String getHistoryTime() {
        return mHistoryTime;
    }

    private void setHistoryTime(String historyTime) {
        mHistoryTime = historyTime;
    }
}