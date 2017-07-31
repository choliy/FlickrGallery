package com.choliy.igor.flickrgallery.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.adapter.SavedAdapter;
import com.choliy.igor.flickrgallery.async.SavedPicLoader;
import com.choliy.igor.flickrgallery.data.FlickrLab;
import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.util.ExtraUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SavedActivity extends BroadcastActivity implements
        LoaderManager.LoaderCallbacks<List<GalleryItem>> {

    @BindView(R.id.layout_no_pic) LinearLayout mEmptyList;
    @BindView(R.id.progress_view) AVLoadingIndicatorView mProgress;
    @BindView(R.id.rv_saved) RecyclerView mRecyclerView;
    private SavedAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        ButterKnife.bind(this);
        restartLoader();

        mAdapter = new SavedAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @OnClick({R.id.ic_return_saved, R.id.ic_delete_pictures})
    public void onBackClick(View view) {
        switch (view.getId()) {
            case R.id.ic_return_saved:
                finish();
                break;
            case R.id.ic_delete_pictures:
                if (mAdapter.getItemCount() == FlickrConstants.INT_ZERO)
                    ExtraUtils.showInfo(view, getString(R.string.text_delete_nothing));
                else showDialog();
                break;
        }
    }

    @Override
    public Loader<List<GalleryItem>> onCreateLoader(int id, Bundle args) {
        return new SavedPicLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<GalleryItem>> loader, List<GalleryItem> data) {
        if (data.size() == FlickrConstants.INT_ZERO) mEmptyList.setVisibility(View.VISIBLE);
        mProgress.smoothToHide();
        mAdapter.setItems(data);
    }

    @Override
    public void onLoaderReset(Loader<List<GalleryItem>> loader) {
        mAdapter.setItems(new ArrayList<GalleryItem>());
    }

    private void restartLoader() {
        getSupportLoaderManager().restartLoader(SavedPicLoader.SAVED_PIC_LOADER_ID, null, this);
    }

    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = View.inflate(this, R.layout.dialog_delete, null);
        final TextView noBtn = (TextView) view.findViewById(R.id.btn_delete_no);
        final TextView yesBtn = (TextView) view.findViewById(R.id.btn_delete_yes);

        builder.setView(view);
        final AlertDialog dialog = builder.show();

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = SavedActivity.this;
                FlickrLab.getInstance(context).deleteAllPictures();
                restartLoader();
                ExtraUtils.showInfo(mRecyclerView, getString(R.string.text_delete_removed));
                dialog.dismiss();
            }
        });
    }
}