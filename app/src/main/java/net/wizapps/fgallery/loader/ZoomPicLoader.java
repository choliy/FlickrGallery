package net.wizapps.fgallery.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.AsyncTaskLoader;

import net.wizapps.fgallery.tool.Constants;

public class ZoomPicLoader extends AsyncTaskLoader<Bitmap> {

    public static final int ZOOM_PIC_LOADER_ID = 333;
    private final byte[] mByteArray;
    private Bitmap mBitmap;

    public ZoomPicLoader(Context context, byte[] byteArray) {
        super(context);
        mByteArray = byteArray;
    }

    @Override
    protected void onStartLoading() {
        if (mBitmap == null) {
            forceLoad();
        } else {
            deliverResult(mBitmap);
        }
    }

    @Override
    public Bitmap loadInBackground() {
        return BitmapFactory.decodeByteArray(mByteArray, Constants.ZERO, mByteArray.length);
    }

    @Override
    public void deliverResult(Bitmap bitmap) {
        mBitmap = bitmap;
        super.deliverResult(bitmap);
    }
}