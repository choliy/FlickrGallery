package com.choliy.igor.flickrgallery.activity;

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
import com.choliy.igor.flickrgallery.util.ExtraUtils;
import com.choliy.igor.flickrgallery.util.FabUtils;
import com.github.clans.fab.FloatingActionMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PictureActivity extends BroadcastActivity {

    @BindView(R.id.fab_menu_picture) FloatingActionMenu mFabMenu;
    @BindView(R.id.picture_ic_back) ImageView mBackButton;
    @BindView(R.id.fence_picture_view) View mFenceView;
    private GalleryItem mItem;

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
        }

        mFabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFabMenu.isOpened()) enablePictureScreen(Boolean.TRUE);
                else enablePictureScreen(Boolean.FALSE);
            }
        });
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
                enablePictureScreen(Boolean.TRUE);
                FlickrLab.getInstance(this).addPicture(mItem);
                ExtraUtils.showShortToast(this, getString(R.string.fab_picture_saved));
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