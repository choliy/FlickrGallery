package com.choliy.igor.galleryforflickr.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.util.Log;

import com.choliy.igor.galleryforflickr.FlickrConstants;
import com.choliy.igor.galleryforflickr.R;
import com.choliy.igor.galleryforflickr.activity.WebActivity;
import com.choliy.igor.galleryforflickr.model.GalleryItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class FabUtils {

    private FabUtils() {
    }

    public static void browserUrl(Context context, String stringUrl) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(stringUrl));
        NavUtils.checkBeforeLaunching(context, browserIntent);
    }

    public static void goWeb(Context context, GalleryItem item) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(FlickrConstants.OWNER_KEY, item.getOwnerName());
        intent.putExtra(FlickrConstants.URI_KEY, item.getItemUri());
        context.startActivity(intent);
    }

    public static void shareUrl(Activity activity, String stringUrl) {
        ShareCompat.IntentBuilder
                .from(activity)
                .setType("text/plain")
                .setChooserTitle(activity.getString(R.string.app_name))
                .setText(stringUrl)
                .startChooser();
    }

    public static void copyData(Context context, String stringData) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(context.getString(R.string.app_name), stringData);
        clipboard.setPrimaryClip(clip);
    }

    public static String getPictureUrl(GalleryItem item, boolean bigPicture) {
        String url;
        String noUrl = FlickrConstants.JSON_NO_SUCH_SIZE;
        if (!item.getBigPicUrl().equals(noUrl) && bigPicture)
            url = item.getBigPicUrl();
        else if (!item.getMediumPicUrl().equals(noUrl))
            url = item.getMediumPicUrl();
        else if (!item.getSmallPicUrl().equals(noUrl))
            url = item.getSmallPicUrl();
        else if (!item.getExtraSmallPicUrl().equals(noUrl)) {
            url = item.getExtraSmallPicUrl();
        } else {
            url = item.getListPicUrl();
        }
        return url;
    }

    public static Bitmap getBitmapFromURL(String picUrl) {
        try {
            URL url = new URL(picUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(Boolean.TRUE);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            Log.e(FabUtils.class.getSimpleName(), e.getMessage());
            return null;
        }
    }
}