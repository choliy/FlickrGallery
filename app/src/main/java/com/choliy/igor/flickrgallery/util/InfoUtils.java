package com.choliy.igor.flickrgallery.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

public final class InfoUtils {

    private InfoUtils() {
    }

    public static void showShortShack(View view, String infoText) {
        Snackbar.make(view, infoText, Snackbar.LENGTH_SHORT).show();
    }

    public static void showShortToast(Context context, String infoText) {
        Toast.makeText(context, infoText, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String infoText) {
        Toast.makeText(context, infoText, Toast.LENGTH_LONG).show();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        return isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
    }
}