package com.choliy.igor.flickrgallery.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ZoomActivity extends AppCompatActivity {

    //@BindView(R.id.loading_bar) ProgressBar mLoading;
    @BindView(R.id.picture_full_screen) PhotoView mPicture;
    private String mPicUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        ButterKnife.bind(this);

        mPicUrl = getIntent().getStringExtra(FlickrConstants.STRING_KEY);
        byte[] byteArray = getIntent().getByteArrayExtra(FlickrConstants.BITMAP_KEY);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, FlickrConstants.INT_ZERO, byteArray.length);
        mPicture.setImageBitmap(bitmap);

        if (savedInstanceState != null)
            mPicUrl = savedInstanceState.getString(FlickrConstants.URL_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadLargeImage();
        //load();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(FlickrConstants.URL_KEY, mPicUrl);
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.picture_full_screen)
    public void onPictureClick() {
        finish();
    }

    private void loadLargeImage() {
        Glide.with(this).load(mPicUrl).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(
                    Exception e,
                    String model,
                    Target<GlideDrawable> target,
                    boolean isFirstResource) {

                return Boolean.FALSE;
            }

            @Override
            public boolean onResourceReady(
                    GlideDrawable resource,
                    String model,
                    Target<GlideDrawable> target,
                    boolean isFromMemoryCache,
                    boolean isFirstResource) {

                //mLoading.setVisibility(View.GONE);
                mPicture.setImageDrawable(resource);
                return Boolean.FALSE;
            }
        }).into(mPicture);
    }

    private void load() {
        Glide.with(this)
                .load(mPicUrl)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        //mLoading.setVisibility(View.GONE);
                        mPicture.setImageBitmap(resource);
                    }
                });
    }
}