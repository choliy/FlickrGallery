package com.choliy.igor.flickrgallery;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GalleryFragment extends Fragment implements GalleryAdapter.OnPhotoHolderListener {

    private int mListPosition = FlickrConstants.DEFAULT_LIST_POSITION;
    private int mPageNumber = FlickrConstants.DEFAULT_PAGE_NUMBER;
    private boolean mDataLoaded;
    private boolean mDataRefreshing;

    private GalleryAdapter mGalleryAdapter;
    private List<GalleryItem> mItems = new ArrayList<>();

    @BindView(R.id.button_top_list) TextView mTopListButton;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;

    public static Fragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Save all fragment data after rotation
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mGalleryAdapter = new GalleryAdapter(getActivity(), mItems, this);
        mRecyclerView.setAdapter(mGalleryAdapter);
        setGridLayoutManager();
        setScrollListener();
        setRefreshLayout();

        if (mDataLoaded) updateUi();
        else fetchData();
    }

    @Override
    public void onRequestedLastItem(int position) {
        mPageNumber++;
        mListPosition = position;
        new FetchItemsTask().execute(mPageNumber);
    }

    @Override
    public void onPhotoClicked(String photoId) {
        String infoText = getString(R.string.text_item_id, photoId);
        FlickrUtils.showInfo(mRecyclerView, infoText);
    }

    @OnClick(R.id.button_top_list)
    public void topList() {
        mRecyclerView.scrollToPosition(FlickrConstants.DEFAULT_LIST_POSITION);
    }

    private void setGridLayoutManager() {
        int screenOrientation = getResources().getConfiguration().orientation;
        if (screenOrientation == Configuration.ORIENTATION_PORTRAIT)
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        else
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
    }

    private void setScrollListener() {
        mRecyclerView.addOnScrollListener(new HidingScrollListener() {
            Toolbar mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

            @Override
            public void onHide() {
                mToolbar.animate()
                        .translationY(-mToolbar.getHeight())
                        .setInterpolator(new AccelerateInterpolator(2));
                FlickrUtils.animateView(getActivity(), mTopListButton, true);
            }

            @Override
            public void onShow() {
                FlickrUtils.animateView(getActivity(), mTopListButton, false);
                mToolbar.animate()
                        .translationY(0)
                        .setInterpolator(new DecelerateInterpolator(2));
            }
        });
    }

    private void setRefreshLayout() {
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDataRefreshing = true;
                mListPosition = FlickrConstants.DEFAULT_LIST_POSITION;
                mPageNumber = FlickrConstants.DEFAULT_PAGE_NUMBER;
                fetchData();
            }
        });

        // Set the offset from top of the screen for SwipeRefreshLayout
        mRefreshLayout.setProgressViewOffset(false, 30, 250);
    }

    private void fetchData() {
        new FetchItemsTask().execute(FlickrConstants.DEFAULT_PAGE_NUMBER);
        mDataLoaded = true;
    }

    private void updateUi() {
        mGalleryAdapter.updateItems(mItems);
        mRecyclerView.scrollToPosition(mListPosition);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private class FetchItemsTask extends AsyncTask<Integer, Void, List<GalleryItem>> {

        @Override
        protected void onPreExecute() {
            if (!mDataRefreshing) mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<GalleryItem> doInBackground(Integer... integers) {
            return new FlickrFetch().fetchItems(integers[0]);
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {

            if (mPageNumber != FlickrConstants.DEFAULT_PAGE_NUMBER) {
                for (int i = 0; i < items.size(); i++) {
                    mItems.add(items.get(i));
                }
            } else mItems = items;

            updateUi();
            mRefreshLayout.setRefreshing(false);
            mDataRefreshing = false;
        }
    }
}