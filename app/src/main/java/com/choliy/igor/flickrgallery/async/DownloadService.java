package com.choliy.igor.flickrgallery.async;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.model.GalleryItem;
import com.choliy.igor.flickrgallery.tool.ImageSaver;
import com.choliy.igor.flickrgallery.util.FabUtils;
import com.choliy.igor.flickrgallery.util.InfoUtils;

import java.io.File;

public class DownloadService extends IntentService {

    public static final String TAG = DownloadService.class.getSimpleName();

    public DownloadService() {
        super(TAG);
    }

    public static Intent newIntent(Context context, GalleryItem item) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(FlickrConstants.ITEM_KEY, item);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Context context = getApplicationContext();
        GalleryItem item = intent.getParcelableExtra(FlickrConstants.ITEM_KEY);
        String picUrl = FabUtils.getPictureUrl(item, Boolean.TRUE);
        Bitmap bitmap = FabUtils.getBitmapFromURL(picUrl);
        String appDirectory = getString(R.string.app_dir);
        String fileName = item.getOwnerName() + File.pathSeparator + item.getOwnerId();

        new ImageSaver(context)
                .setDirectoryName(appDirectory)
                .setFileName(fileName)
                .setExternal(Boolean.TRUE)
                .save(bitmap);

        Log.i(TAG, fileName);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                InfoUtils.showToast(context, getString(R.string.fab_downloaded));
            }
        });
    }
}