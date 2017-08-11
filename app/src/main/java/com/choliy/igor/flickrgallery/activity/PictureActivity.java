package com.choliy.igor.flickrgallery.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.async.DownloadService;
import com.choliy.igor.flickrgallery.data.FlickrLab;
import com.choliy.igor.flickrgallery.fragment.PictureFragment;
import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.util.AnimUtils;
import com.choliy.igor.flickrgallery.util.ExtraUtils;
import com.choliy.igor.flickrgallery.util.FabUtils;
import com.choliy.igor.flickrgallery.util.InfoUtils;
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

    private static final int REQUEST_CODE = 111;
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

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        int zero = FlickrConstants.INT_ZERO;
        boolean requestCodeSame = requestCode == REQUEST_CODE;
        boolean resultsNotEmpty = grantResults.length > zero;
        boolean permissionGranted = grantResults[zero] == PackageManager.PERMISSION_GRANTED;
        if (requestCodeSame && resultsNotEmpty && permissionGranted) onDownloadClick();
        else InfoUtils.showShortToast(this, getString(R.string.text_permission));
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
                onCopyClick();
                break;
            case R.id.fab_save:
                onSaveClick();
                break;
            case R.id.fab_download:
                checkPermissionAndDownload();
                break;
        }
    }

    private void onCopyClick() {
        String url = mItem.getItemUri();
        FabUtils.copyUrl(this, url);
        InfoUtils.showShortToast(this, getString(R.string.fab_copied, url));
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

    private void checkPermissionAndDownload() {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_CODE);
        else onDownloadClick();
    }

    private void onDownloadClick() {
        if (mPictureDownloaded) {
            InfoUtils.showShortToast(this, getString(R.string.text_already_downloaded));
        } else {
            mPictureDownloaded = Boolean.TRUE;
            InfoUtils.showShortToast(this, getString(R.string.fab_downloading));
            startService(DownloadService.newIntent(this, mItem));
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
}