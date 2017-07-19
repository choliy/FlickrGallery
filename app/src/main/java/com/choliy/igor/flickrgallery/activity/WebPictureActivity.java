package com.choliy.igor.flickrgallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.fragment.WebPictureFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebPictureActivity extends BroadcastActivity {

    @BindView(R.id.text_picture_title) TextView mPictureTitle;
    @BindView(R.id.layout_no_uri) LinearLayout mNoUriLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_web);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String itemUri = FlickrConstants.STRING_EMPTY;
        if (intent != null) {
            String pictureTitle = intent.getStringExtra(FlickrConstants.TITLE_KEY);
            itemUri = intent.getStringExtra(FlickrConstants.URI_KEY);
            if (pictureTitle.isEmpty()) pictureTitle = getString(R.string.text_picture_empty);
            mPictureTitle.setText(pictureTitle);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.web_view_container);
        if (fragment == null) {
            if (itemUri.isEmpty()) {
                mNoUriLayout.setVisibility(View.VISIBLE);
                return;
            }
            fragment = WebPictureFragment.newInstance(itemUri);
            fragmentManager
                    .beginTransaction()
                    .add(R.id.web_view_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    @OnClick(R.id.ic_return_picture)
    public void onReturnClick() {
        finishActivity();
    }

    private void finishActivity() {
        NavUtils.navigateUpFromSameTask(this);
        finish();
    }
}