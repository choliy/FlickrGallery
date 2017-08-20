package com.choliy.igor.galleryforflickr.event;

public class ToolbarTypeEvent {

    private boolean mToolbarSearch;

    public ToolbarTypeEvent(boolean toolbarSearch) {
        setToolbarSearch(toolbarSearch);
    }

    public boolean isToolbarSearch() {
        return mToolbarSearch;
    }

    private void setToolbarSearch(boolean toolbarSearch) {
        mToolbarSearch = toolbarSearch;
    }
}