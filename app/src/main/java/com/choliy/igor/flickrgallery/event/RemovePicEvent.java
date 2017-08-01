package com.choliy.igor.flickrgallery.event;

public class RemovePicEvent {

    private boolean mShowDialog;

    public RemovePicEvent(boolean showDialog) {
        setShowDialog(showDialog);
    }

    public boolean isShowDialog() {
        return mShowDialog;
    }

    private void setShowDialog(boolean showDialog) {
        mShowDialog = showDialog;
    }
}