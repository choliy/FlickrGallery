package com.choliy.igor.flickrgallery;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment implements GalleryAdapter.OnPhotoHolderListener {

    private int mListPosition = FlickrConstants.DEFAULT_LIST_POSITION;
    private int mPageNumber = FlickrConstants.DEFAULT_PAGE_NUMBER;
    private boolean mDataLoaded;

    private ProgressBar mGalleryProgressBar;
    private RecyclerView mPhotoRecyclerView;
    private GalleryAdapter mGalleryAdapter;
    private List<GalleryItem> mItems = new ArrayList<>();

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
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mGalleryProgressBar = (ProgressBar) view.findViewById(R.id.gallery_progress_bar);
        mPhotoRecyclerView = (RecyclerView) view.findViewById(R.id.gallery_recycler_view);
        mGalleryAdapter = new GalleryAdapter(getActivity(), mItems, this);
        mPhotoRecyclerView.setAdapter(mGalleryAdapter);
        setGridLayoutManager();

        if (mDataLoaded) {
            updateUi();
        } else {
            new FetchItemsTask().execute(FlickrConstants.DEFAULT_PAGE_NUMBER);
            mDataLoaded = true;
        }
    }

    @Override
    public void onRequestedLastItem(int position) {
        mPageNumber++;
        mListPosition = position;
        new FetchItemsTask().execute(mPageNumber);

        String infoText = getString(R.string.text_items_loaded, String.valueOf(mPageNumber));
        FlickrUtils.showLongInfo(mPhotoRecyclerView, infoText);
    }

    @Override
    public void onPhotoClicked(String photoId) {
        String infoText = getString(R.string.text_item_id, photoId);
        FlickrUtils.showInfo(mPhotoRecyclerView, infoText);
    }

    private void setGridLayoutManager() {
        int screenOrientation = getResources().getConfiguration().orientation;
        if (screenOrientation == Configuration.ORIENTATION_PORTRAIT)
            mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        else
            mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
    }

    private void updateUi() {
        mGalleryAdapter.updateItems(mItems);
        mPhotoRecyclerView.scrollToPosition(mListPosition);
        mGalleryProgressBar.setVisibility(View.INVISIBLE);
    }

    private class FetchItemsTask extends AsyncTask<Integer, Void, List<GalleryItem>> {

        @Override
        protected void onPreExecute() {
            mGalleryProgressBar.setVisibility(View.VISIBLE);
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
        }
    }
}