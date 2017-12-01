package net.wizapps.fgallery.view;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import net.wizapps.fgallery.tool.Constants;
import net.wizapps.fgallery.tool.Events;

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
        EventBus.getDefault().post(new Events.SwipePositionEvent(viewHolder.getAdapterPosition()));
    }
}