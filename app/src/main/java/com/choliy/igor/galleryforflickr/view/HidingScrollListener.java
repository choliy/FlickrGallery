package com.choliy.igor.galleryforflickr.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.choliy.igor.galleryforflickr.tool.Constants;

public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {

    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = Constants.ZERO;
    private boolean controlsVisible = Boolean.TRUE;

    public abstract void onHide();

    public abstract void onShow();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager())
                .findFirstVisibleItemPosition();

        // show views if first item is on first visible position and views are hidden
        if (firstVisibleItem == Constants.ZERO) {
            if (!controlsVisible) {
                onShow();
                controlsVisible = Boolean.TRUE;
            }
        } else {
            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                onHide();
                controlsVisible = Boolean.FALSE;
                scrolledDistance = Constants.ZERO;
            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                onShow();
                controlsVisible = Boolean.TRUE;
                scrolledDistance = Constants.ZERO;
            }
        }

        if ((controlsVisible && dy > Constants.ZERO) || (!controlsVisible && dy < Constants.ZERO)) {
            scrolledDistance += dy;
        }
    }
}