package com.choliy.igor.flickrgallery.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.choliy.igor.flickrgallery.FlickrConstants;

public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {

    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = FlickrConstants.INT_ZERO;
    private boolean controlsVisible = Boolean.TRUE;

    public abstract void onHide();

    public abstract void onShow();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager())
                .findFirstVisibleItemPosition();

        // show views if first item is on first visible position and views are hidden
        if (firstVisibleItem == FlickrConstants.INT_ZERO) {
            if (!controlsVisible) {
                onShow();
                controlsVisible = Boolean.TRUE;
            }
        } else {
            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                onHide();
                controlsVisible = Boolean.FALSE;
                scrolledDistance = FlickrConstants.INT_ZERO;
            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                onShow();
                controlsVisible = Boolean.TRUE;
                scrolledDistance = FlickrConstants.INT_ZERO;
            }
        }

        if ((controlsVisible && dy > FlickrConstants.INT_ZERO)
                || (!controlsVisible && dy < FlickrConstants.INT_ZERO)) {
            scrolledDistance += dy;
        }
    }
}