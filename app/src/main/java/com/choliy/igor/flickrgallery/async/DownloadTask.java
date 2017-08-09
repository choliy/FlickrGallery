package com.choliy.igor.flickrgallery.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.util.FabUtils;
import com.choliy.igor.flickrgallery.util.InfoUtils;
import com.choliy.igor.flickrgallery.view.ImageSaver;

public class DownloadTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private GalleryItem mItem;

    public DownloadTask(Context context, GalleryItem item) {
        mContext = context;
        mItem = item;
    }

    @Override
    protected void onPreExecute() {
        InfoUtils.showShortToast(mContext, mContext.getString(R.string.fab_downloading));
    }

    @Override
    protected Void doInBackground(Void... params) {
        String picUrl = FabUtils.getPictureUrl(mContext, mItem, Boolean.TRUE);
        Bitmap bitmap = FabUtils.getBitmapFromURL(picUrl);
        String appDirectory = mContext.getString(R.string.app_dir);
        String fileName = mItem.getOwnerName() + "-" + mItem.getOwnerId();

        new ImageSaver(mContext)
                .setDirectoryName(appDirectory)
                .setFileName(fileName)
                .setExternal(Boolean.TRUE)
                .save(bitmap);

        Log.i(DownloadTask.class.getSimpleName(), fileName);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        InfoUtils.showShortToast(mContext, mContext.getString(R.string.fab_downloaded));
        mContext = null;
    }
}