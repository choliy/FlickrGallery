package com.choliy.igor.flickrgallery.event;

public class ToolbarEvent {

    private boolean mShowToolbar;

    public ToolbarEvent(boolean showToolbar) {
        setShowToolbar(showToolbar);
    }

    public boolean isShowToolbar() {
        return mShowToolbar;
    }

    private void setShowToolbar(boolean showToolbar) {
        mShowToolbar = showToolbar;
    }
}