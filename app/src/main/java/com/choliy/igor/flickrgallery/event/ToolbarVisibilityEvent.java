package com.choliy.igor.flickrgallery.event;

public class ToolbarVisibilityEvent {

    private boolean mShowToolbar;

    public ToolbarVisibilityEvent(boolean showToolbar) {
        setShowToolbar(showToolbar);
    }

    public boolean isShowToolbar() {
        return mShowToolbar;
    }

    private void setShowToolbar(boolean showToolbar) {
        mShowToolbar = showToolbar;
    }
}