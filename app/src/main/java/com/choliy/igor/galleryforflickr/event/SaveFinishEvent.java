package com.choliy.igor.galleryforflickr.event;

public class SaveFinishEvent {

    private boolean mClearHistoryBase;

    public SaveFinishEvent(boolean clearHistoryBase) {
        setClearHistoryBase(clearHistoryBase);
    }

    public boolean isClearHistoryBase() {
        return mClearHistoryBase;
    }

    private void setClearHistoryBase(boolean clearHistoryBase) {
        mClearHistoryBase = clearHistoryBase;
    }
}