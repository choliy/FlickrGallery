package com.choliy.igor.galleryforflickr.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.choliy.igor.galleryforflickr.FlickrConstants;

public class ItemDividerDecoration extends RecyclerView.ItemDecoration {

    private int mGridMarginPx;
    private int mGridSize;
    private boolean mNeedLeftSpacing;

    public ItemDividerDecoration(int gridMarginPx, int gridSize) {
        mGridMarginPx = gridMarginPx;
        mGridSize = gridSize;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int frameWidth = (int) ((parent.getWidth() - (float) mGridMarginPx * (mGridSize - FlickrConstants.INT_ONE)) / mGridSize);
        int padding = parent.getWidth() / mGridSize - frameWidth;
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();

        if (itemPosition < mGridSize) outRect.top = FlickrConstants.INT_ZERO;
        else outRect.top = mGridMarginPx;

        if (itemPosition % mGridSize == FlickrConstants.INT_ZERO) {
            outRect.left = FlickrConstants.INT_ZERO;
            outRect.right = padding;
            mNeedLeftSpacing = true;
        } else if ((itemPosition + FlickrConstants.INT_ONE) % mGridSize == FlickrConstants.INT_ZERO) {
            mNeedLeftSpacing = false;
            outRect.right = FlickrConstants.INT_ZERO;
            outRect.left = padding;
        } else if (mNeedLeftSpacing) {
            mNeedLeftSpacing = false;
            outRect.left = mGridMarginPx - padding;
            if ((itemPosition + FlickrConstants.INT_TWO) % mGridSize == FlickrConstants.INT_ZERO)
                outRect.right = mGridMarginPx - padding;
            else outRect.right = mGridMarginPx / FlickrConstants.INT_TWO;
        } else if ((itemPosition + FlickrConstants.INT_TWO) % mGridSize == FlickrConstants.INT_ZERO) {
            mNeedLeftSpacing = false;
            outRect.left = mGridMarginPx / FlickrConstants.INT_TWO;
            outRect.right = mGridMarginPx - padding;
        } else {
            mNeedLeftSpacing = false;
            outRect.left = mGridMarginPx / FlickrConstants.INT_TWO;
            outRect.right = mGridMarginPx / FlickrConstants.INT_TWO;
        }
        outRect.bottom = FlickrConstants.INT_ZERO;
    }
}