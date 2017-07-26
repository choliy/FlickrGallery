package com.choliy.igor.flickrgallery.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.fragment.PictureFragment;
import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.util.AnimUtils;
import com.choliy.igor.flickrgallery.util.FabUtils;
import com.github.clans.fab.FloatingActionMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PictureActivity extends BroadcastActivity {

    @BindView(R.id.fab_menu_picture) FloatingActionMenu mFabMenu;
    @BindView(R.id.picture_ic_back) ImageView mBackButton;
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
        if (mFabMenu.isOpened()) mFabMenu.close(Boolean.TRUE);
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
                mFabMenu.close(Boolean.TRUE);
                FabUtils.goWeb(this, mItem);
                break;
            case R.id.fab_share:
                mFabMenu.close(Boolean.TRUE);
                FabUtils.shareUrl(this, mItem.getItemUri());
                break;
            case R.id.fab_copy:
                mFabMenu.close(Boolean.TRUE);
                String url = mItem.getItemUri();
                FabUtils.copyUrl(this, url);
                Toast.makeText(this, getString(R.string.fab_copied, url), Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_save:
                mFabMenu.close(Boolean.TRUE);
                // TODO save
                break;
            case R.id.fab_download:
                mFabMenu.close(Boolean.TRUE);
                // TODO download
                break;
        }
    }

    private void animateViews(boolean show) {
        AnimUtils.animateBackButton(this, mBackButton, show);
        AnimUtils.animateView(this, mFabMenu, show);
    }
}