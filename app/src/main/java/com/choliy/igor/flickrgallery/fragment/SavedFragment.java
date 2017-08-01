package com.choliy.igor.flickrgallery.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.adapter.SavedAdapter;
import com.choliy.igor.flickrgallery.async.SavedPicLoader;
import com.choliy.igor.flickrgallery.data.FlickrLab;
import com.choliy.igor.flickrgallery.event.RemovePicEvent;
import com.choliy.igor.flickrgallery.event.ToolbarEvent;
import com.choliy.igor.flickrgallery.event.TopListEvent;
import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.util.DialogUtils;
import com.choliy.igor.flickrgallery.util.InfoUtils;
import com.choliy.igor.flickrgallery.view.HidingScrollListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedFragment extends EventFragment implements
        LoaderManager.LoaderCallbacks<List<GalleryItem>> {

    @BindView(R.id.layout_no_pic) LinearLayout mEmptyList;
    @BindView(R.id.progress_view) AVLoadingIndicatorView mProgress;
    @BindView(R.id.rv_saved) RecyclerView mRecyclerView;

    private List<GalleryItem> mSavedPictures;
    private SavedAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, container, Boolean.FALSE);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mAdapter = new SavedAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper touchHelper = new ItemTouchHelper(new OnSavedPicSwipeCallback());
        touchHelper.attachToRecyclerView(mRecyclerView);
        setOnScrollListener();
        getActivity().getSupportLoaderManager().initLoader(SavedPicLoader.SAVED_PIC_LOADER_ID, null, this);
    }

    @Subscribe
    public void onEvent(RemovePicEvent event) {
        if (event.isShowDialog()) {
            if (mAdapter.getItemCount() == FlickrConstants.INT_ZERO)
                InfoUtils.showShortShack(mRecyclerView, getString(R.string.text_delete_nothing));
            else DialogUtils.deleteDialog(getActivity(), new DeletePicTask());
        }
    }

    @Subscribe
    public void onEvent(TopListEvent event) {
        mRecyclerView.scrollToPosition(event.getScrollPosition());
    }

    @Override
    public Loader<List<GalleryItem>> onCreateLoader(int id, Bundle args) {
        return new SavedPicLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<GalleryItem>> loader, List<GalleryItem> data) {
        mProgress.smoothToHide();
        mAdapter.setItems(data);
        checkData();
    }

    @Override
    public void onLoaderReset(Loader<List<GalleryItem>> loader) {
        mAdapter.setItems(new ArrayList<GalleryItem>());
    }

    private void setOnScrollListener() {
        mRecyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                EventBus.getDefault().post(new ToolbarEvent(Boolean.FALSE));
            }

            @Override
            public void onShow() {
                EventBus.getDefault().post(new ToolbarEvent(Boolean.TRUE));
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
                new RestorePicTask().execute();
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
                InfoUtils.showShortShack(mRecyclerView, getString(R.string.text_delete_single_restored));
            }
        });
        snackbar.show();
    }

    private void checkData() {
        if (mAdapter.getItemCount() == FlickrConstants.INT_ZERO)
            mEmptyList.setVisibility(View.VISIBLE);
        else mEmptyList.setVisibility(View.INVISIBLE);
    }

    public class DeletePicTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgress.smoothToShow();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mSavedPictures = mAdapter.getItems();
            FlickrLab.getInstance(getActivity()).deleteAllPictures();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            restartLoader();
            restoreAllPictures();
        }
    }

    private class RestorePicTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mEmptyList.setVisibility(View.INVISIBLE);
            mProgress.smoothToShow();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            FlickrLab.getInstance(getActivity()).restoreAllPictures(mSavedPictures);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            restartLoader();
            InfoUtils.showShortShack(mRecyclerView, getString(R.string.text_delete_all_restored));
        }
    }

    private class OnSavedPicSwipeCallback extends ItemTouchHelper.SimpleCallback {

        private int mPosition;

        OnSavedPicSwipeCallback() {
            super(FlickrConstants.INT_ZERO, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
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
            mPosition = viewHolder.getAdapterPosition();
            GalleryItem item = mAdapter.removeItem(mPosition);
            FlickrLab.getInstance(getActivity()).deletePicture(item.getDbId());
            restorePicture(mPosition, item);
            checkData();
        }
    }
}