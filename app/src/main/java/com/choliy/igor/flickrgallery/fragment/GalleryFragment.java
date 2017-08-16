package com.choliy.igor.flickrgallery.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.activity.PictureActivity;
import com.choliy.igor.flickrgallery.adapter.GalleryAdapter;
import com.choliy.igor.flickrgallery.event.ItemLastEvent;
import com.choliy.igor.flickrgallery.event.ItemPositionEvent;
import com.choliy.igor.flickrgallery.event.ToolbarTypeEvent;
import com.choliy.igor.flickrgallery.event.ToolbarVisibilityEvent;
import com.choliy.igor.flickrgallery.event.TopListEvent;
import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.tool.FlickrFetch;
import com.choliy.igor.flickrgallery.util.ExtraUtils;
import com.choliy.igor.flickrgallery.util.InfoUtils;
import com.choliy.igor.flickrgallery.util.PrefUtils;
import com.choliy.igor.flickrgallery.view.HidingScrollListener;
import com.choliy.igor.flickrgallery.view.ItemDividerDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GalleryFragment extends EventFragment {

    @BindView(R.id.progress_view) AVLoadingIndicatorView mProgressView;
    @BindView(R.id.rv_gallery) RecyclerView mRvGallery;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.layout_no_connection) LinearLayout mConnectionLayout;
    @BindView(R.id.layout_no_results) LinearLayout mResultsLayout;

    private GalleryAdapter mGalleryAdapter;
    public static List<GalleryItem> sItems = new ArrayList<>();
    private int mListPosition = FlickrConstants.DEFAULT_LIST_POSITION;
    private int mPageNumber = FlickrConstants.DEFAULT_PAGE_NUMBER;
    private boolean mNoMoreData;
    private boolean mDataLoaded;
    private boolean mDataRefreshing;

    @Override
    int layoutRes() {
        return R.layout.fragment_gallery;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setRecyclerView();
        setOnScrollListener();
        setRefreshLayout();
        setRvStyle();

        if (mDataLoaded && isConnected()) updateUi();
        else fetchData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            final int scrollPosition = data.getExtras().getInt(FlickrConstants.POSITION_KEY);
            mRvGallery.scrollToPosition(scrollPosition);
            highlightPicture(scrollPosition);
        }
    }

    @Subscribe
    public void onEvent(ItemPositionEvent event) {
        Intent intent = PictureActivity.newInstance(getActivity(), event.getPosition(), Boolean.FALSE);
        startActivityForResult(intent, PictureActivity.REQUEST_CODE);
    }

    @Subscribe
    public void onEvent(ItemLastEvent event) {
        if (!mNoMoreData) {
            ++mPageNumber;
            mListPosition = event.getPosition();
            fetchData();
        }
    }

    @Subscribe
    public void onEvent(TopListEvent event) {
        mRvGallery.scrollToPosition(event.getScrollPosition());
    }

    private void setRecyclerView() {
        String gridSize = PrefUtils.getGridSettings(getActivity());
        String gridStyle = PrefUtils.getStyleSettings(getActivity());
        boolean isAnimationOn = PrefUtils.getAnimationSettings(getActivity());

        mGalleryAdapter = new GalleryAdapter(sItems, gridSize, gridStyle, isAnimationOn);
        mRvGallery.setAdapter(mGalleryAdapter);
        mRvGallery.setHasFixedSize(Boolean.TRUE);
    }

    private void setOnScrollListener() {
        mRvGallery.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                EventBus.getDefault().post(new ToolbarVisibilityEvent(Boolean.FALSE));
            }

            @Override
            public void onShow() {
                EventBus.getDefault().post(new ToolbarVisibilityEvent(Boolean.TRUE));
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
                ExtraUtils.hideKeyboard(getActivity());
                EventBus.getDefault().post(new ToolbarTypeEvent(Boolean.FALSE));
            }
        });

        // Set the offset from top of the screen for SwipeRefreshLayout
        mRefreshLayout.setProgressViewOffset(
                Boolean.FALSE, // scaling animation
                40,            // top position of the loading indicator
                300);          // max scrolling bottom position of current indicator
    }

    private int[] setGridLayoutManager() {
        String gridSize = PrefUtils.getGridSettings(getActivity());
        int sizeVertical = Character.getNumericValue(gridSize.charAt(FlickrConstants.INT_ZERO));
        int sizeHorizontal = Character.getNumericValue(gridSize.charAt(FlickrConstants.INT_ONE));

        int screenOrientation = InfoUtils.getOrientation(getActivity());
        if (screenOrientation == Configuration.ORIENTATION_PORTRAIT)
            mRvGallery.setLayoutManager(new GridLayoutManager(getActivity(), sizeVertical));
        else
            mRvGallery.setLayoutManager(new GridLayoutManager(getActivity(), sizeHorizontal));

        return new int[]{screenOrientation, sizeVertical, sizeHorizontal};
    }

    private void setRvStyle() {
        String savedStyle = PrefUtils.getStyleSettings(getActivity());
        String dividerStyle = getString(R.string.pref_grid_style_value_2);
        String cardStyle = getString(R.string.pref_grid_style_value_3);
        int[] gridSize = setGridLayoutManager();
        int orientation = gridSize[FlickrConstants.INT_ZERO];
        int verticalGrid = gridSize[FlickrConstants.INT_ONE];
        int horizontalGrid = gridSize[FlickrConstants.INT_TWO];
        int gridMargin = Math.round(getResources().getDimension(R.dimen.dimen_list_divider_margin));
        ItemDividerDecoration decoration;

        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            decoration = new ItemDividerDecoration(gridMargin, verticalGrid);
        else decoration = new ItemDividerDecoration(gridMargin, horizontalGrid);

        if (savedStyle.equals(dividerStyle)) {
            mRvGallery.addItemDecoration(decoration);
        } else if (savedStyle.equals(cardStyle)) {
            int padding = Math.round(getResources().getDimension(R.dimen.dimen_list_card_margin));
            int paddingTop = InfoUtils.getActionBarHeight(getActivity()) + padding;
            mRvGallery.setPadding(padding, paddingTop, padding, padding);
        }
    }

    private void fetchData() {
        if (isConnected()) new FetchItemsTask().execute();
    }

    private boolean isConnected() {
        boolean connected = InfoUtils.isNetworkConnected(getActivity());
        if (connected) {
            mConnectionLayout.setVisibility(View.GONE);
            mRvGallery.setVisibility(View.VISIBLE);
        } else {
            mConnectionLayout.setVisibility(View.VISIBLE);
            mResultsLayout.setVisibility(View.GONE);
            mRvGallery.setVisibility(View.GONE);
            mProgressView.smoothToHide();
            mRefreshLayout.setRefreshing(Boolean.FALSE);
            EventBus.getDefault().post(new ToolbarVisibilityEvent(Boolean.TRUE));
        }
        return connected;
    }

    private void updateUi() {
        mGalleryAdapter.updateItems(sItems);
        mRvGallery.scrollToPosition(mListPosition);
        mProgressView.smoothToHide();
        mRefreshLayout.setRefreshing(Boolean.FALSE);
        mDataRefreshing = Boolean.FALSE;
        mDataLoaded = Boolean.TRUE;

        if (mGalleryAdapter.getItemCount() == FlickrConstants.INT_ZERO) {
            mResultsLayout.setVisibility(View.VISIBLE);
            mConnectionLayout.setVisibility(View.GONE);
            EventBus.getDefault().post(new ToolbarVisibilityEvent(Boolean.TRUE));
        } else mResultsLayout.setVisibility(View.GONE);
    }

    private void highlightPicture(final int scrollPosition) {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                GalleryAdapter.PictureHolder holder = (GalleryAdapter.PictureHolder)
                        mRvGallery.findViewHolderForAdapterPosition(scrollPosition);
                holder.highlightPicture();
            }
        };
        int milliseconds = getResources().getInteger(R.integer.anim_duration_1000);
        handler.postDelayed(runnable, milliseconds);
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {

        @Override
        protected void onPreExecute() {
            if (!mDataRefreshing) mProgressView.smoothToShow();
        }

        @Override
        protected List<GalleryItem> doInBackground(Void... voids) {
            return new FlickrFetch().downloadGallery(getActivity(), mPageNumber);
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mNoMoreData = items.isEmpty();
            if (mPageNumber > FlickrConstants.DEFAULT_PAGE_NUMBER) {
                for (GalleryItem item : items) {
                    sItems.add(item);
                }
            } else sItems = items;
            updateUi();
        }
    }
}