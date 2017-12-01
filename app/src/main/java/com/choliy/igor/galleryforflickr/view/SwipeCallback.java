package com.choliy.igor.galleryforflickr.view;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.choliy.igor.galleryforflickr.tool.Constants;
import com.choliy.igor.galleryforflickr.event.SwipePositionEvent;

import org.greenrobot.eventbus.EventBus;

public class SwipeCallback extends ItemTouchHelper.SimpleCallback {

    public SwipeCallback() {
        super(Constants.ZERO, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(
            RecyclerView recyclerView,
            RecyclerView.ViewHolder viewHolder,
            RecyclerView.ViewHolder target) {

        return Boolean.FALSE;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        EventBus.getDefault().post(new SwipePositionEvent(viewHolder.getAdapterPosition()));
    }
}