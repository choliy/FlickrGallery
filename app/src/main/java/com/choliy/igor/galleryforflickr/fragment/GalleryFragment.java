package com.choliy.igor.galleryforflickr.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.choliy.igor.galleryforflickr.R;
import com.choliy.igor.galleryforflickr.activity.PictureActivity;
import com.choliy.igor.galleryforflickr.adapter.GalleryAdapter;
import com.choliy.igor.galleryforflickr.async.FetchPicturesTask;
import com.choliy.igor.galleryforflickr.base.BaseFragment;
import com.choliy.igor.galleryforflickr.model.GalleryItem;
import com.choliy.igor.galleryforflickr.tool.Constants;
import com.choliy.igor.galleryforflickr.tool.Events;
import com.choliy.igor.galleryforflickr.util.ExtraUtils;
import com.choliy.igor.galleryforflickr.util.InfoUtils;
import com.choliy.igor.galleryforflickr.util.PrefUtils;
import com.choliy.igor.galleryforflickr.view.HidingScrollListener;
import com.choliy.igor.galleryforflickr.view.ItemDividerDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GalleryFragment extends BaseFragment {

    @BindView(R.id.progress_view) AVLoadingIndicatorView mProgressView;
    @BindView(R.id.rv_gallery) RecyclerView mRvGallery;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.layout_no_connection) LinearLayout mConnectionLayout;
    @BindView(R.id.layout_no_results) LinearLayout mResultsLayout;

    private GalleryAdapter mGalleryAdapter;
    public static List<GalleryItem> sGalleryItems = new ArrayList<>();
    private int mPageNumber = Constants.DEFAULT_PAGE_NUMBER;
    private boolean mNoMoreData;
    private boolean mDataLoaded;
    private boolean mDataRefreshing;

    @Override
    protected int layoutRes() {
        return R.layout.fragment_gallery;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setRecyclerView();
        setOnScrollListener();
        setRefreshLayout();
        setRvStyle();

        if (mDataLoaded && isConnected()) {
            updateUi();
        } else {
            fetchData();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            final int scrollPosition = data.getIntExtra(Constants.POSITION_KEY, Constants.ZERO);
            mRvGallery.scrollToPosition(scrollPosition);
            highlightPicture(scrollPosition);
        }
    }

    @Subscribe
    public void onEvent(Events.ItemPositionEvent event) {
        Intent intent = PictureActivity.newInstance(getActivity(), event.getPosition(), Boolean.FALSE);
        startActivityForResult(intent, PictureActivity.REQUEST_CODE);
    }

    @Subscribe
    public void onEvent(Events.ItemLastEvent event) {
        if (!mNoMoreData) {
            ++mPageNumber;
            fetchData();
        }
    }

    @Subscribe
    public void onEvent(Events.TopListEvent event) {
        mRvGallery.scrollToPosition(Constants.ZERO);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(Events.AnimPrefEvent event) {
        mGalleryAdapter.setAnimation(event.isAnimationOn());
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe
    public void onEvent(Events.FetchStartEvent event) {
        if (!mDataRefreshing) {
            mProgressView.smoothToShow();
            mGalleryAdapter.setClickable(Boolean.FALSE);
        }
    }

    @Subscribe
    public void onEvent(Events.FetchFinishEvent event) {
        mNoMoreData = event.getPictures().isEmpty();
        if (mPageNumber > Constants.DEFAULT_PAGE_NUMBER) {
            sGalleryItems.addAll(event.getPictures());
        } else {
            sGalleryItems = event.getPictures();
        }
        updateUi();
    }


    private void setRecyclerView() {
        String gridSize = PrefUtils.getGridSettings(getActivity());
        String gridStyle = PrefUtils.getStyleSettings(getActivity());
        boolean isAnimationOn = PrefUtils.getAnimationSettings(getActivity());

        mGalleryAdapter = new GalleryAdapter(sGalleryItems, gridSize, gridStyle, isAnimationOn);
        mRvGallery.setAdapter(mGalleryAdapter);
        mRvGallery.setHasFixedSize(Boolean.TRUE);
    }

    private void setOnScrollListener() {
        mRvGallery.addOnScrollListener(new HidingScrollListener() {
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

    private void setRefreshLayout() {
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDataRefreshing = Boolean.TRUE;
                mPageNumber = Constants.DEFAULT_PAGE_NUMBER;
                fetchData();
                ExtraUtils.hideKeyboard(getActivity());
                EventBus.getDefault().post(new Events.ToolbarTypeEvent());
            }
        });

        // Set the offset from top of the screen for SwipeRefreshLayout
        mRefreshLayout.setProgressViewOffset(
                Boolean.FALSE, // scaling animation
                40, // top position of the loading indicator
                300); // max scrolling bottom position of current indicator
    }

    private int[] setGridLayoutManager() {
        String gridSize = PrefUtils.getGridSettings(getActivity());
        int sizeVertical = Character.getNumericValue(gridSize.charAt(Constants.ZERO));
        int sizeHorizontal = Character.getNumericValue(gridSize.charAt(Constants.ONE));

        int screenOrientation = InfoUtils.getOrientation(getActivity());
        if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
            mRvGallery.setLayoutManager(new GridLayoutManager(getActivity(), sizeVertical));
        } else {
            mRvGallery.setLayoutManager(new GridLayoutManager(getActivity(), sizeHorizontal));
        }

        return new int[]{screenOrientation, sizeVertical, sizeHorizontal};
    }

    private void setRvStyle() {
        String savedStyle = PrefUtils.getStyleSettings(getActivity());
        String dividerStyle = getString(R.string.pref_grid_style_value_2);
        String cardStyle = getString(R.string.pref_grid_style_value_3);
        int[] gridSize = setGridLayoutManager();
        int orientation = gridSize[Constants.ZERO];
        int verticalGrid = gridSize[Constants.ONE];
        int horizontalGrid = gridSize[Constants.TWO];
        int gridMargin = Math.round(getResources().getDimension(R.dimen.dimen_list_divider_margin));
        ItemDividerDecoration decoration;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            decoration = new ItemDividerDecoration(gridMargin, verticalGrid);
        } else {
            decoration = new ItemDividerDecoration(gridMargin, horizontalGrid);
        }

        if (savedStyle.equals(dividerStyle)) {
            mRvGallery.addItemDecoration(decoration);
        } else if (savedStyle.equals(cardStyle)) {
            int padding = Math.round(getResources().getDimension(R.dimen.dimen_list_card_margin));
            int paddingTop = InfoUtils.getActionBarHeight(getActivity()) + padding;
            mRvGallery.setPadding(padding, paddingTop, padding, padding);
        }
    }

    private void fetchData() {
        if (isConnected()) new FetchPicturesTask(mPageNumber).execute(getActivity());
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
            EventBus.getDefault().post(new Events.ToolbarVisibilityEvent(Boolean.TRUE));
        }
        return connected;
    }

    private void updateUi() {
        mGalleryAdapter.updateItems(sGalleryItems);
        mGalleryAdapter.setClickable(Boolean.TRUE);
        mProgressView.smoothToHide();
        mRefreshLayout.setRefreshing(Boolean.FALSE);
        mDataRefreshing = Boolean.FALSE;
        mDataLoaded = Boolean.TRUE;

        if (mGalleryAdapter.getItemCount() == Constants.ZERO) {
            mResultsLayout.setVisibility(View.VISIBLE);
            mConnectionLayout.setVisibility(View.GONE);
            EventBus.getDefault().post(new Events.ToolbarVisibilityEvent(Boolean.TRUE));
        } else {
            mResultsLayout.setVisibility(View.GONE);
        }
    }

    private void highlightPicture(final int scrollPosition) {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                GalleryAdapter.PictureHolder holder = (GalleryAdapter.PictureHolder)
                        mRvGallery.findViewHolderForAdapterPosition(scrollPosition);
                if (holder != null) holder.highlightPicture();
            }
        };
        int milliseconds = getResources().getInteger(R.integer.anim_duration_1000);
        handler.postDelayed(runnable, milliseconds);
    }
}