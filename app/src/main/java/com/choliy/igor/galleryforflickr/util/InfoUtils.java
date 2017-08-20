package com.choliy.igor.galleryforflickr.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.choliy.igor.galleryforflickr.R;

public final class InfoUtils {

    private InfoUtils() {
    }

    public static void showShack(View view, String infoText) {
        Snackbar.make(view, infoText, Snackbar.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String infoText) {
        Toast.makeText(context, infoText, Toast.LENGTH_SHORT).show();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        return isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
    }

    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, Boolean.TRUE))
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        else
            return Math.round(context.getResources().getDimension(R.dimen.dimen_toolbar_height));
    }

    public static int getOrientation(Context context) {
        return context.getResources().getConfiguration().orientation;
    }
}