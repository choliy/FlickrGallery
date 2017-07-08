package com.choliy.igor.flickrgallery.model;

public class HistoryItem {

    private String mId;
    private String mHistoryTitle;
    private String mHistoryDate;
    private String mHistoryTime;

    public HistoryItem() {
    }

    public HistoryItem(String historyTitle, String historyDate, String historyTime) {
        mHistoryTitle = historyTitle;
        mHistoryDate = historyDate;
        mHistoryTime = historyTime;
    }

    public HistoryItem(String id, String historyTitle, String historyDate, String historyTime) {
        mId = id;
        mHistoryTitle = historyTitle;
        mHistoryDate = historyDate;
        mHistoryTime = historyTime;
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

    public void setHistoryTitle(String historyTitle) {
        mHistoryTitle = historyTitle;
    }

    public String getHistoryDate() {
        return mHistoryDate;
    }

    public void setHistoryDate(String historyDate) {
        mHistoryDate = historyDate;
    }

    public String getHistoryTime() {
        return mHistoryTime;
    }

    public void setHistoryTime(String historyTime) {
        mHistoryTime = historyTime;
    }
}