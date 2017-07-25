package com.choliy.igor.flickrgallery.activity;

import android.os.Bundle;

import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.fragment.PictureFragment;
import com.choliy.igor.flickrgallery.interfaces.FlickrConstants;
import com.choliy.igor.flickrgallery.model.GalleryItem;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PictureActivity extends BroadcastActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);

        GalleryItem item = getIntent().getParcelableExtra(FlickrConstants.ITEM_KEY);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.picture_container, PictureFragment.newInstance(item))
                    .commit();
        }
    }

    @OnClick(R.id.picture_ic_back)
    public void onBackClicked() {
        finish();
    }
}