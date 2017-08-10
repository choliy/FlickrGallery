package com.choliy.igor.flickrgallery.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.AsyncTaskLoader;

import com.choliy.igor.flickrgallery.util.FabUtils;

public class SinglePicLoader extends AsyncTaskLoader<Bitmap> {

    public static final int SINGLE_PIC_LOADER_ID = 111;
    private Bitmap mBitmap;
    private String mPicUrl;

    public SinglePicLoader(Context context, String picUrl) {
        super(context);
        mPicUrl = picUrl;
    }

    @Override
    protected void onStartLoading() {
        if (mBitmap == null) forceLoad();
        else deliverResult(mBitmap);
    }

    @Override
    public Bitmap loadInBackground() {
        return FabUtils.getBitmapFromURL(mPicUrl);
    }

    @Override
    public void deliverResult(Bitmap bitmap) {
        mBitmap = bitmap;
        super.deliverResult(bitmap);
    }
}