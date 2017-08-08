package com.choliy.igor.flickrgallery.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.data.FlickrLab;
import com.choliy.igor.flickrgallery.fragment.PictureFragment;
import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.util.AnimUtils;
import com.choliy.igor.flickrgallery.util.ExtraUtils;
import com.choliy.igor.flickrgallery.util.FabUtils;
import com.choliy.igor.flickrgallery.util.InfoUtils;
import com.choliy.igor.flickrgallery.view.ImageSaver;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PictureActivity extends BroadcastActivity {

    @BindView(R.id.fab_web) FloatingActionButton mFabWeb;
    @BindView(R.id.fab_share) FloatingActionButton mFabShare;
    @BindView(R.id.fab_copy) FloatingActionButton mFabCopy;
    @BindView(R.id.fab_save) FloatingActionButton mFabSave;
    @BindView(R.id.fab_download) FloatingActionButton mFabDownload;
    @BindView(R.id.fab_menu_pic) FloatingActionMenu mFabMenu;
    @BindView(R.id.picture_ic_back) ImageView mBackButton;
    @BindView(R.id.fence_picture_view) View mFenceView;

    private GalleryItem mItem;
    private boolean mPictureSaved;
    private boolean mPictureDownloaded;
    private boolean mIsFabOpened;

    public static Intent getInstance(Context context, GalleryItem item, boolean savedPicture) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(FlickrConstants.ITEM_KEY, item);
        intent.putExtra(FlickrConstants.SAVE_KEY, savedPicture);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
        checkOrientationSize();
        onMenuClick();

        mItem = getIntent().getParcelableExtra(FlickrConstants.ITEM_KEY);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.picture_container, PictureFragment.newInstance(mItem))
                    .commit();
        } else {
            mPictureSaved = savedInstanceState.getBoolean(FlickrConstants.PICTURE_SAVED_KEY);
            mPictureDownloaded = savedInstanceState.getBoolean(FlickrConstants.PICTURE_DOWNLOADED_KEY);
            mIsFabOpened = savedInstanceState.getBoolean(FlickrConstants.MENU_OPENED_KEY);
        }

        boolean savedPicture = getIntent().getBooleanExtra(FlickrConstants.SAVE_KEY, Boolean.FALSE);
        if (savedPicture) mFabMenu.removeMenuButton(mFabSave);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsFabOpened) enablePictureScreen(Boolean.FALSE);
        else animateViews(Boolean.TRUE);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (mFabMenu.isOpened()) enablePictureScreen(Boolean.TRUE);
        else super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(FlickrConstants.PICTURE_SAVED_KEY, mPictureSaved);
        outState.putBoolean(FlickrConstants.PICTURE_DOWNLOADED_KEY, mPictureDownloaded);
        outState.putBoolean(FlickrConstants.MENU_OPENED_KEY, mIsFabOpened);
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.picture_ic_back)
    public void onBackClicked() {
        finish();
    }

    @OnClick({R.id.fab_web, R.id.fab_share, R.id.fab_copy, R.id.fab_save, R.id.fab_download})
    public void onFabClicked(View view) {
        enablePictureScreen(Boolean.TRUE);
        switch (view.getId()) {
            case R.id.fab_web:
                FabUtils.goWeb(this, mItem);
                break;
            case R.id.fab_share:
                FabUtils.shareUrl(this, mItem.getItemUri());
                break;
            case R.id.fab_copy:
                String url = mItem.getItemUri();
                FabUtils.copyUrl(this, url);
                InfoUtils.showShortToast(this, getString(R.string.fab_copied, url));
                break;
            case R.id.fab_save:
                onSaveClick();
                break;
            case R.id.fab_download:
                onDownloadClick();
                break;
        }
    }

    private void onSaveClick() {
        if (mPictureSaved) {
            InfoUtils.showShortToast(this, getString(R.string.text_already_saved));
        } else {
            mPictureSaved = Boolean.TRUE;
            FlickrLab.getInstance(this).addPicture(mItem);
            InfoUtils.showShortToast(this, getString(R.string.fab_picture_saved));
        }
    }

    private void onDownloadClick() {
        if (mPictureDownloaded) {
            InfoUtils.showShortToast(this, getString(R.string.text_already_downloaded));
        } else {
            mPictureDownloaded = Boolean.TRUE;
            String pictureUrl = FabUtils.getPictureUrl(this, mItem, Boolean.TRUE);
            FabUtils.downloadPicture(this, pictureUrl, new DownloadTask());
        }
    }

    private void onMenuClick() {
        mFabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFabMenu.isOpened()) enablePictureScreen(Boolean.TRUE);
                else enablePictureScreen(Boolean.FALSE);
            }
        });
    }

    private void animateViews(boolean show) {
        AnimUtils.animateBackButton(this, mBackButton, show);
        AnimUtils.animateView(this, mFabMenu, show);
    }

    private void enablePictureScreen(boolean enable) {
        mIsFabOpened = !enable;
        if (enable) {
            mFenceView.setVisibility(View.INVISIBLE);
            mFabMenu.close(Boolean.TRUE);
        } else {
            mFenceView.setVisibility(View.VISIBLE);
            mFabMenu.open(Boolean.TRUE);
        }
    }

    private void checkOrientationSize() {
        int orientation = ExtraUtils.getOrientation(this);
        int size;

        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            size = FloatingActionButton.SIZE_NORMAL;
        else
            size = FloatingActionButton.SIZE_MINI;

        mFabWeb.setButtonSize(size);
        mFabShare.setButtonSize(size);
        mFabCopy.setButtonSize(size);
        mFabSave.setButtonSize(size);
        mFabDownload.setButtonSize(size);
    }

    public class DownloadTask extends AsyncTask<Bitmap, Void, Void> {

        @Override
        protected void onPreExecute() {
            Context context = PictureActivity.this;
            InfoUtils.showShortToast(context, getString(R.string.fab_downloading));
        }

        @Override
        protected Void doInBackground(Bitmap... bitmap) {
            Context context = PictureActivity.this;
            String fileName = mItem.getOwnerName() + mItem.getOwnerId();
            new ImageSaver(context)
                    .setDirectoryName(context.getString(R.string.app_dir))
                    .setFileName(fileName)
                    .setExternal(Boolean.TRUE)
                    .save(bitmap[FlickrConstants.INT_ZERO]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Context context = PictureActivity.this;
            InfoUtils.showShortToast(context, getString(R.string.fab_downloaded));
        }
    }
}