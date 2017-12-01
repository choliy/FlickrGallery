package net.wizapps.fgallery.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;

import com.wang.avi.AVLoadingIndicatorView;

import net.wizapps.fgallery.R;
import net.wizapps.fgallery.adapter.SavedAdapter;
import net.wizapps.fgallery.async.PicDeleteTask;
import net.wizapps.fgallery.async.PicRestoreTask;
import net.wizapps.fgallery.base.BaseFragment;
import net.wizapps.fgallery.data.FlickrLab;
import net.wizapps.fgallery.loader.SavedPicLoader;
import net.wizapps.fgallery.model.GalleryItem;
import net.wizapps.fgallery.tool.Constants;
import net.wizapps.fgallery.tool.Events;
import net.wizapps.fgallery.util.DialogUtils;
import net.wizapps.fgallery.util.InfoUtils;
import net.wizapps.fgallery.view.HidingScrollListener;
import net.wizapps.fgallery.view.SwipeCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SavedFragment extends BaseFragment implements
        LoaderManager.LoaderCallbacks<List<GalleryItem>> {

    @BindView(R.id.layout_no_pic) LinearLayout mEmptyList;
    @BindView(R.id.progress_view) AVLoadingIndicatorView mProgress;
    @BindView(R.id.rv_saved) RecyclerView mRecyclerView;

    public static List<GalleryItem> sSavedItems;
    private List<GalleryItem> mRestoreItems;
    private SavedAdapter mAdapter;

    @Override
    protected int layoutRes() {
        return R.layout.fragment_saved;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setRecyclerView();
        setOnScrollListener();
        getActivity()
                .getSupportLoaderManager()
                .initLoader(SavedPicLoader.SAVED_PIC_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<GalleryItem>> onCreateLoader(int id, Bundle args) {
        return new SavedPicLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<GalleryItem>> loader, List<GalleryItem> data) {
        mProgress.smoothToHide();
        mAdapter.setItems(data);
        sSavedItems = data;
        checkData();
    }

    @Override
    public void onLoaderReset(Loader<List<GalleryItem>> loader) {
        mAdapter.setItems(new ArrayList<GalleryItem>());
    }

    @Subscribe
    public void onEvent(Events.RemovePicEvent event) {
        if (mAdapter.getItemCount() == Constants.ZERO) {
            InfoUtils.showShack(mRecyclerView, getString(R.string.text_delete_nothing));
        } else {
            DialogUtils.deleteDialog(getActivity(), new PicDeleteTask());
        }
    }

    @Subscribe
    public void onEvent(Events.TopListEvent event) {
        mRecyclerView.scrollToPosition(Constants.ZERO);
    }

    @Subscribe
    public void onEvent(Events.SwipePositionEvent event) {
        GalleryItem item = mAdapter.removeItem(event.getPosition());
        FlickrLab.getInstance(getActivity()).deletePicture(item.getDbId());
        restorePicture(event.getPosition(), item);
        checkData();
    }

    @Subscribe
    public void onEvent(Events.DeleteStartEvent event) {
        mProgress.smoothToShow();
        mRestoreItems = sSavedItems;
    }

    @Subscribe
    public void onEvent(Events.DeleteFinishEvent event) {
        restartLoader();
        restoreAllPictures();
    }

    @Subscribe
    public void onEvent(Events.RestoreStartEvent event) {
        mEmptyList.setVisibility(View.INVISIBLE);
        mProgress.smoothToShow();
    }

    @Subscribe
    public void onEvent(Events.RestoreFinishEvent event) {
        restartLoader();
        InfoUtils.showShack(mRecyclerView, getString(R.string.text_delete_all_restored));
    }

    private void setRecyclerView() {
        mAdapter = new SavedAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper touchHelper = new ItemTouchHelper(new SwipeCallback());
        touchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void setOnScrollListener() {
        mRecyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                EventBus.getDefault().post(new Events.ToolbarVisibilityEvent(Boolean.FALSE));
            }

            @Override
            public void onShow() {
                EventBus.getDefault().post(new Events.ToolbarVisibilityEvent(Boolean.TRUE));
            }
        });
    }

    private void restartLoader() {
        getActivity().getSupportLoaderManager().restartLoader(SavedPicLoader.SAVED_PIC_LOADER_ID, null, this);
    }

    private void restoreAllPictures() {
        String text = getString(R.string.text_delete_all_removed);
        Snackbar snackbar = Snackbar.make(mRecyclerView, text, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.dialog_undo_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PicRestoreTask(mRestoreItems).execute(getActivity());
            }
        });
        snackbar.show();
    }

    private void restorePicture(final int position, final GalleryItem item) {
        String text = getString(R.string.text_delete_single_removed);
        Snackbar snackbar = Snackbar.make(mRecyclerView, text, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.dialog_undo_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.restoreItem(position, item);
                FlickrLab.getInstance(getActivity()).restorePicture(item);
                checkData();
                InfoUtils.showShack(mRecyclerView, getString(R.string.text_delete_single_restored));
            }
        });
        snackbar.show();
    }

    private void checkData() {
        boolean adapterNotEmpty = mAdapter.getItemCount() == Constants.ZERO;
        mEmptyList.setVisibility(adapterNotEmpty ? View.VISIBLE : View.INVISIBLE);
    }
}