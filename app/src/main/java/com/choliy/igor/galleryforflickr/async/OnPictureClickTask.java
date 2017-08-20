package com.choliy.igor.galleryforflickr.async;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.choliy.igor.galleryforflickr.FlickrConstants;
import com.choliy.igor.galleryforflickr.activity.ZoomActivity;
import com.choliy.igor.galleryforflickr.model.GalleryItem;
import com.choliy.igor.galleryforflickr.tool.ImageSaver;
import com.choliy.igor.galleryforflickr.util.FabUtils;

import java.io.ByteArrayOutputStream;

public class OnPictureClickTask extends AsyncTask<Void, Void, byte[]> {

    private Context mContext;
    private Bitmap mBitmap;
    private GalleryItem mItem;

    public OnPictureClickTask(Context context, Bitmap bitmap, GalleryItem item) {
        mContext = context;
        mBitmap = bitmap;
        mItem = item;
    }

    @Override
    protected byte[] doInBackground(Void... params) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, ImageSaver.QUALITY_MID, stream);
        return stream.toByteArray();
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        String picUrl = FabUtils.getPictureUrl(mItem, Boolean.TRUE);
        Intent intent = new Intent(mContext, ZoomActivity.class);
        intent.putExtra(FlickrConstants.STRING_KEY, picUrl);
        intent.putExtra(FlickrConstants.BITMAP_KEY, bytes);
        mContext.startActivity(intent);
        mContext = null;
    }
}