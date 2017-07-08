package com.choliy.igor.flickrgallery.fragment;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.adapter.GalleryAdapter;
import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.tool.FlickrFetch;
import com.choliy.igor.flickrgallery.tool.HidingScrollListener;
import com.choliy.igor.flickrgallery.util.AnimUtils;
import com.choliy.igor.flickrgallery.util.FlickrUtils;
import com.choliy.igor.flickrgallery.util.PrefUtils;

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

    @BindView(R.id.image_top_list) ImageView mTopListView;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.rv_gallery) RecyclerView mRvGallery;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.no_connection) LinearLayout mConnectionLayout;
    @BindView(R.id.no_results) LinearLayout mResultsLayout;

    public static Fragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, Boolean.FALSE);
        ButterKnife.bind(this, view);
        setupUi();
        return view;
    }

    @Override
    public void onRequestedLastItem(int position) {
        ++mPageNumber;
        mListPosition = position;
        fetchData();
    }

    @Override
    public void onPhotoClicked(String photoId) {
        String infoText = "Photo id - " + photoId;
        FlickrUtils.showInfo(mRvGallery, infoText);
    }

    @OnClick(R.id.image_top_list)
    public void topList() {
        mRvGallery.scrollToPosition(FlickrConstants.DEFAULT_LIST_POSITION);
    }

    private void setupUi() {
        mGalleryAdapter = new GalleryAdapter(getActivity(), mItems, this);
        mRvGallery.setAdapter(mGalleryAdapter);
        mRvGallery.setHasFixedSize(Boolean.TRUE);
        setGridLayoutManager();
        setScrollListener();
        setRefreshLayout();

        if (mDataLoaded && isConnected()) updateUi();
        else fetchData();
    }

    private void setGridLayoutManager() {
        String gridSize = PrefUtils.getGridSettings(getActivity());
        int sizeVertical = Character.getNumericValue(gridSize.charAt(0));
        int sizeHorizontal = Character.getNumericValue(gridSize.charAt(1));

        int screenOrientation = getResources().getConfiguration().orientation;
        if (screenOrientation == Configuration.ORIENTATION_PORTRAIT)
            mRvGallery.setLayoutManager(new GridLayoutManager(getActivity(), sizeVertical));
        else
            mRvGallery.setLayoutManager(new GridLayoutManager(getActivity(), sizeHorizontal));
    }

    private void setScrollListener() {
        mRvGallery.addOnScrollListener(new HidingScrollListener() {

            @Override
            public void onHide() {
                AnimUtils.animateView(getActivity(), mTopListView, Boolean.TRUE);
                AnimUtils.animateToolbarVisibility(getActivity(), Boolean.FALSE);
            }

            @Override
            public void onShow() {
                AnimUtils.animateView(getActivity(), mTopListView, Boolean.FALSE);
                AnimUtils.animateToolbarVisibility(getActivity(), Boolean.TRUE);
            }
        });
    }

    private void setRefreshLayout() {
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDataRefreshing = Boolean.TRUE;
                mPageNumber = FlickrConstants.DEFAULT_PAGE_NUMBER;
                mListPosition = FlickrConstants.DEFAULT_LIST_POSITION;
                fetchData();
                FlickrUtils.hideKeyboard(getActivity());
            }
        });

        // Set the offset from top of the screen for SwipeRefreshLayout
        mRefreshLayout.setProgressViewOffset(
                Boolean.FALSE, // scaling animation
                30, // top position of the loading indicator
                270); // max scrolling bottom position of current indicator
    }

    private void fetchData() {
        if (isConnected()) {
            String storedText = PrefUtils.getStoredQuery(getActivity());
            new FetchItemsTask().execute(storedText);
        }
    }

    private boolean isConnected() {
        boolean connected = FlickrUtils.isNetworkConnected(getActivity());
        if (connected) {
            mConnectionLayout.setVisibility(View.GONE);
            mRvGallery.setVisibility(View.VISIBLE);
        } else {
            mConnectionLayout.setVisibility(View.VISIBLE);
            mResultsLayout.setVisibility(View.GONE);
            mRvGallery.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mRefreshLayout.setRefreshing(Boolean.FALSE);
            AnimUtils.animateToolbarVisibility(getActivity(), Boolean.TRUE);
        }
        return connected;
    }

    private void updateUi() {
        mGalleryAdapter.updateItems(mItems);
        mRvGallery.scrollToPosition(mListPosition);
        mProgressBar.setVisibility(View.GONE);
        mRefreshLayout.setRefreshing(Boolean.FALSE);
        mDataRefreshing = Boolean.FALSE;
        mDataLoaded = Boolean.TRUE;

        if (mGalleryAdapter.getItemCount() == 0) {
            mResultsLayout.setVisibility(View.VISIBLE);
            mConnectionLayout.setVisibility(View.GONE);
            AnimUtils.animateToolbarVisibility(getActivity(), Boolean.TRUE);
        } else
            mResultsLayout.setVisibility(View.GONE);
    }

    private class FetchItemsTask extends AsyncTask<String, Void, List<GalleryItem>> {

        @Override
        protected void onPreExecute() {
            if (!mDataRefreshing) mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<GalleryItem> doInBackground(String... params) {
            return new FlickrFetch().downloadGallery(getActivity(), params[0], mPageNumber);
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            if (mPageNumber > FlickrConstants.DEFAULT_PAGE_NUMBER) {
                for (int i = 0; i < items.size(); i++) {
                    mItems.add(items.get(i));
                }
            } else mItems = items;

            updateUi();
        }
    }
}