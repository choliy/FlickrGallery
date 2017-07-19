package com.choliy.igor.flickrgallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.widget.TextView;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.fragment.PictureFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PictureActivity extends BroadcastActivity {

    @BindView(R.id.text_picture_title) TextView mPictureTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            String pictureTitle = intent.getStringExtra(FlickrConstants.TITLE_KEY);
            if (pictureTitle.isEmpty()) pictureTitle = getString(R.string.text_picture_empty);
            mPictureTitle.setText(pictureTitle);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.web_view_container);
        if (fragment == null) {
            fragment = PictureFragment.newInstance();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.web_view_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @OnClick(R.id.ic_return_picture)
    public void onReturnClick() {
        NavUtils.navigateUpFromSameTask(this);
    }
}