package com.choliy.igor.flickrgallery.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.util.ExtraUtils;
import com.github.chrisbanes.photoview.PhotoView;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ZoomActivity extends AppCompatActivity implements RequestListener {

    @BindView(R.id.progress_view) AVLoadingIndicatorView mProgress;
    @BindView(R.id.picture_full_screen) PhotoView mPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        ButterKnife.bind(this);

        String url = getIntent().getStringExtra(FlickrConstants.PICTURE_KEY);
        ExtraUtils.loadPicture(this, url, mPicture, this);
    }

    @OnClick(R.id.picture_full_screen)
    public void onPictureClick() {
        finish();
    }

    @Override
    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
        return Boolean.FALSE;
    }

    @Override
    public boolean onResourceReady(
            Object resource,
            Object model,
            Target target,
            boolean isFromMemoryCache,
            boolean isFirstResource) {

        mProgress.smoothToHide();
        return Boolean.FALSE;
    }
}