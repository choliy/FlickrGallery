package com.choliy.igor.flickrgallery.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.activity.WebPictureActivity;
import com.choliy.igor.flickrgallery.model.GalleryItem;

public final class FabUtils {

    private FabUtils() {
    }

    public static void browserUrl(Context context, String stringUrl) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(stringUrl));
        NavUtils.checkBeforeLaunching(context, browserIntent);
    }

    public static void goWeb(Context context, GalleryItem item) {
        Intent intent = new Intent(context, WebPictureActivity.class);
        intent.putExtra(FlickrConstants.TITLE_KEY, item.getTitle());
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

    public static void copyUrl(Context context, String stringUrl) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(context.getString(R.string.app_name), stringUrl);
        clipboard.setPrimaryClip(clip);
    }
}