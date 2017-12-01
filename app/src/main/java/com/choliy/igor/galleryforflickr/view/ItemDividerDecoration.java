package com.choliy.igor.galleryforflickr.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.choliy.igor.galleryforflickr.tool.Constants;

public class ItemDividerDecoration extends RecyclerView.ItemDecoration {

    private final int mGridMarginPx;
    private final int mGridSize;
    private boolean mNeedLeftSpacing;

    public ItemDividerDecoration(int gridMarginPx, int gridSize) {
        mGridMarginPx = gridMarginPx;
        mGridSize = gridSize;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int frameWidth = (int) ((parent.getWidth() - (float) mGridMarginPx * (mGridSize - Constants.ONE)) / mGridSize);
        int padding = parent.getWidth() / mGridSize - frameWidth;
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();

        if (itemPosition < mGridSize) {
            outRect.top = Constants.ZERO;
        } else {
            outRect.top = mGridMarginPx;
        }

        if (itemPosition % mGridSize == Constants.ZERO) {
            outRect.left = Constants.ZERO;
            outRect.right = padding;
            mNeedLeftSpacing = true;
        } else if ((itemPosition + Constants.ONE) % mGridSize == Constants.ZERO) {
            mNeedLeftSpacing = false;
            outRect.right = Constants.ZERO;
            outRect.left = padding;
        } else if (mNeedLeftSpacing) {
            mNeedLeftSpacing = false;
            outRect.left = mGridMarginPx - padding;
            if ((itemPosition + Constants.TWO) % mGridSize == Constants.ZERO) {
                outRect.right = mGridMarginPx - padding;
            } else {
                outRect.right = mGridMarginPx / Constants.TWO;
            }
        } else if ((itemPosition + Constants.TWO) % mGridSize == Constants.ZERO) {
            mNeedLeftSpacing = false;
            outRect.left = mGridMarginPx / Constants.TWO;
            outRect.right = mGridMarginPx - padding;
        } else {
            mNeedLeftSpacing = false;
            outRect.left = mGridMarginPx / Constants.TWO;
            outRect.right = mGridMarginPx / Constants.TWO;
        }
        outRect.bottom = Constants.ZERO;
    }
}