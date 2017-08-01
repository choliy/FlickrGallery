package com.choliy.igor.flickrgallery.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.data.FlickrLab;
import com.choliy.igor.flickrgallery.fragment.PictureFragment;
import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.util.AnimUtils;
import com.choliy.igor.flickrgallery.util.FabUtils;
import com.choliy.igor.flickrgallery.util.InfoUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PictureActivity extends BroadcastActivity {

    @BindView(R.id.fab_save) FloatingActionButton mFabSave;
    @BindView(R.id.fab_menu_picture) FloatingActionMenu mFabMenu;
    @BindView(R.id.picture_ic_back) ImageView mBackButton;
    @BindView(R.id.fence_picture_view) View mFenceView;
    private GalleryItem mItem;
    private boolean mPictureSaved;

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

        mItem = getIntent().getParcelableExtra(FlickrConstants.ITEM_KEY);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.picture_container, PictureFragment.newInstance(mItem))
                    .commit();
        } else mPictureSaved = savedInstanceState.getBoolean(FlickrConstants.PICTURE_SAVED_KEY);

        mFabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFabMenu.isOpened()) enablePictureScreen(Boolean.TRUE);
                else enablePictureScreen(Boolean.FALSE);
            }
        });

        boolean savedPicture = getIntent().getBooleanExtra(FlickrConstants.SAVE_KEY, Boolean.FALSE);
        if (savedPicture) mFabMenu.removeMenuButton(mFabSave);
    }

    @Override
    protected void onStart() {
        super.onStart();
        animateViews(Boolean.TRUE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        animateViews(Boolean.FALSE);
    }

    @Override
    public void onBackPressed() {
        if (mFabMenu.isOpened()) enablePictureScreen(Boolean.TRUE);
        else super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(FlickrConstants.PICTURE_SAVED_KEY, mPictureSaved);
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.picture_ic_back)
    public void onBackClicked() {
        finish();
    }

    @OnClick({R.id.fab_go_web, R.id.fab_share, R.id.fab_copy, R.id.fab_save, R.id.fab_download})
    public void onFabClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_go_web:
                enablePictureScreen(Boolean.TRUE);
                FabUtils.goWeb(this, mItem);
                break;
            case R.id.fab_share:
                enablePictureScreen(Boolean.TRUE);
                FabUtils.shareUrl(this, mItem.getItemUri());
                break;
            case R.id.fab_copy:
                enablePictureScreen(Boolean.TRUE);
                String url = mItem.getItemUri();
                FabUtils.copyUrl(this, url);
                Toast.makeText(this, getString(R.string.fab_copied, url), Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_save:
                if (mPictureSaved) {
                    InfoUtils.showShortToast(this, getString(R.string.text_already_saved));
                } else {
                    mPictureSaved = Boolean.TRUE;
                    enablePictureScreen(Boolean.TRUE);
                    FlickrLab.getInstance(this).addPicture(mItem);
                    InfoUtils.showShortToast(this, getString(R.string.fab_picture_saved));
                }
                break;
            case R.id.fab_download:
                enablePictureScreen(Boolean.TRUE);
                // TODO download
                break;
        }
    }

    private void animateViews(boolean show) {
        AnimUtils.animateBackButton(this, mBackButton, show);
        AnimUtils.animateView(this, mFabMenu, show);
    }

    private void enablePictureScreen(boolean enable) {
        if (enable) {
            mFenceView.setVisibility(View.INVISIBLE);
            mFabMenu.close(Boolean.TRUE);
        } else {
            mFenceView.setVisibility(View.VISIBLE);
            mFabMenu.open(Boolean.TRUE);
        }
    }
}