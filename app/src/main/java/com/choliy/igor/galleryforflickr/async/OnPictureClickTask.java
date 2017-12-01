package com.choliy.igor.galleryforflickr.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.choliy.igor.galleryforflickr.event.PictureClickEvent;
import com.choliy.igor.galleryforflickr.model.GalleryItem;
import com.choliy.igor.galleryforflickr.tool.ImageSaver;
import com.choliy.igor.galleryforflickr.util.FabUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;

public class OnPictureClickTask extends AsyncTask<Void, Void, byte[]> {

    private Bitmap mBitmap;
    private GalleryItem mItem;

    public OnPictureClickTask(Bitmap bitmap, GalleryItem item) {
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
        EventBus.getDefault().post(new PictureClickEvent(picUrl, bytes));
    }
}