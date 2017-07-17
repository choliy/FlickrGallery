package com.choliy.igor.flickrgallery.tool;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

    private int mGridMarginPx;
    private int mGridSize;
    private boolean mNeedLeftSpacing;

    public ItemOffsetDecoration(int gridMarginPx, int gridSize) {
        mGridMarginPx = gridMarginPx;
        mGridSize = gridSize;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int frameWidth = (int) ((parent.getWidth() - (float) mGridMarginPx * (mGridSize - 1)) / mGridSize);
        int padding = parent.getWidth() / mGridSize - frameWidth;
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();

        if (itemPosition < mGridSize) outRect.top = 0;
        else outRect.top = mGridMarginPx;

        if (itemPosition % mGridSize == 0) {
            outRect.left = 0;
            outRect.right = padding;
            mNeedLeftSpacing = true;
        } else if ((itemPosition + 1) % mGridSize == 0) {
            mNeedLeftSpacing = false;
            outRect.right = 0;
            outRect.left = padding;
        } else if (mNeedLeftSpacing) {
            mNeedLeftSpacing = false;
            outRect.left = mGridMarginPx - padding;
            if ((itemPosition + 2) % mGridSize == 0) outRect.right = mGridMarginPx - padding;
            else outRect.right = mGridMarginPx / 2;
        } else if ((itemPosition + 2) % mGridSize == 0) {
            mNeedLeftSpacing = false;
            outRect.left = mGridMarginPx / 2;
            outRect.right = mGridMarginPx - padding;
        } else {
            mNeedLeftSpacing = false;
            outRect.left = mGridMarginPx / 2;
            outRect.right = mGridMarginPx / 2;
        }
        outRect.bottom = 0;
    }
}