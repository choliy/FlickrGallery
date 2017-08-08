package com.choliy.igor.flickrgallery.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ShareCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.activity.PictureActivity;
import com.choliy.igor.flickrgallery.activity.WebActivity;
import com.choliy.igor.flickrgallery.model.GalleryItem;

public final class FabUtils {

    private FabUtils() {
    }

    public static void browserUrl(Context context, String stringUrl) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(stringUrl));
        NavUtils.checkBeforeLaunching(context, browserIntent);
    }

    public static void goWeb(Context context, GalleryItem item) {
        Intent intent = new Intent(context, WebActivity.class);
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

    public static void downloadPicture(
            final Context context,
            String stringUrl,
            final PictureActivity.DownloadTask downloadTask) {

        Glide.with(context)
                .load(stringUrl)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        downloadTask.execute(bitmap);
                    }
                });
    }

    public static String getPictureUrl(Context context, GalleryItem item, boolean bigPicture) {
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
            smallPicture(context);
            url = item.getListPicUrl();
        }
        return url;
    }

    private static void smallPicture(Context context) {
        InfoUtils.showLongToast(context, context.getString(R.string.text_picture_small));
    }
}