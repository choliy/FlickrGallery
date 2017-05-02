package com.choliy.igor.flickrgallery;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public final class FlickrUtils {

    public static void showInfo(View view, String infoText) {
        Snackbar.make(view, infoText, Snackbar.LENGTH_SHORT).show();
    }

    public static void animateView(Context context, View view, boolean show) {
        int resId;
        if (show) resId = R.anim.animation_show;
        else resId = R.anim.animation_hide;

        Animation animation = AnimationUtils.loadAnimation(context, resId);
        view.startAnimation(animation);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}