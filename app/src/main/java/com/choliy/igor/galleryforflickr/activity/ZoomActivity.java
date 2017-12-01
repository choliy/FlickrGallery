package com.choliy.igor.galleryforflickr.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.choliy.igor.galleryforflickr.R;
import com.choliy.igor.galleryforflickr.base.BaseActivity;
import com.choliy.igor.galleryforflickr.loader.ZoomPicLoader;
import com.choliy.igor.galleryforflickr.tool.Constants;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.OnClick;

public class ZoomActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Bitmap> {

    @BindView(R.id.loading_bar) ProgressBar mLoading;
    @BindView(R.id.picture_full_screen) PhotoView mPicture;

    public static Intent newInstance(Context context, String picUrl, byte[] bytes) {
        Intent intent = new Intent(context, ZoomActivity.class);
        intent.putExtra(Constants.STRING_KEY, picUrl);
        intent.putExtra(Constants.BITMAP_KEY, bytes);
        return intent;
    }

    @Override
    public int layoutRes() {
        return R.layout.activity_zoom;
    }

    @Override
    public void setUi(Bundle savedInstanceState) {
        getSupportLoaderManager().initLoader(ZoomPicLoader.ZOOM_PIC_LOADER_ID, null, this);
        mLoading.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(this, R.color.colorAccent),
                PorterDuff.Mode.SRC_IN);
    }

    @Override
    public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
        byte[] byteArray = getIntent().getByteArrayExtra(Constants.BITMAP_KEY);
        return new ZoomPicLoader(this, byteArray);
    }

    @Override
    public void onLoadFinished(Loader<Bitmap> loader, Bitmap bitmap) {
        String picUrl = getIntent().getStringExtra(Constants.STRING_KEY);
        loadLargePicture(picUrl, bitmap);
    }

    @Override
    public void onLoaderReset(Loader<Bitmap> loader) {}

    @OnClick(R.id.picture_full_screen)
    public void onPictureClick() {
        finish();
    }

    private void loadLargePicture(String picUrl, Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        Glide.with(this)
                .load(picUrl)
                .dontAnimate()
                .placeholder(drawable)
                .listener(new RequestListener<String, GlideDrawable>() {
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

                        mLoading.setVisibility(View.GONE);
                        return Boolean.FALSE;
                    }
                }).into(mPicture);
    }
}