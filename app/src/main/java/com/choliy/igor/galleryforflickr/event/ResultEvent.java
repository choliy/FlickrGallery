package com.choliy.igor.galleryforflickr.event;

public class ResultEvent {

    private boolean mResultOk;

    public ResultEvent(boolean resultOk) {
        setResultOk(resultOk);
    }

    public boolean isResultOk() {
        return mResultOk;
    }

    private void setResultOk(boolean resultOk) {
        mResultOk = resultOk;
    }
}