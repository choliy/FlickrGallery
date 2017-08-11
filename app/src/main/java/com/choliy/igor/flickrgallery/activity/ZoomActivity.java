package com.choliy.igor.flickrgallery.activity;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.loader.ZoomPicLoader;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ZoomActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Bitmap> {

    @BindView(R.id.loading_bar) ProgressBar mLoading;
    @BindView(R.id.picture_full_screen) PhotoView mPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        ButterKnife.bind(this);
        getSupportLoaderManager().initLoader(ZoomPicLoader.ZOOM_PIC_LOADER_ID, null, this);
        mLoading.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(this, R.color.colorAccent),
                PorterDuff.Mode.SRC_IN);
    }

    @Override
    public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
        byte[] byteArray = getIntent().getByteArrayExtra(FlickrConstants.BITMAP_KEY);
        return new ZoomPicLoader(this, byteArray);
    }

    @Override
    public void onLoadFinished(Loader<Bitmap> loader, Bitmap bitmap) {
        String picUrl = getIntent().getStringExtra(FlickrConstants.STRING_KEY);
        loadLargePicture(picUrl, bitmap);
    }

    @Override
    public void onLoaderReset(Loader<Bitmap> loader) {
    }

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