package com.choliy.igor.flickrgallery.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import com.choliy.igor.flickrgallery.R;

public final class FlickrUtils {

    public static void showInfo(View view, String infoText) {
        Snackbar.make(view, infoText, Snackbar.LENGTH_SHORT).show();
    }

    public static void animateView(Context context, View view, boolean show) {
        int resId;
        if (show) {
            resId = R.anim.animation_show;
            view.setVisibility(View.VISIBLE);
        } else {
            resId = R.anim.animation_hide;
            view.setVisibility(View.INVISIBLE);
        }

        Animation animation = AnimationUtils.loadAnimation(context, resId);
        view.startAnimation(animation);
    }

    public static void animateToolbar(Context context, boolean show) {
        Toolbar toolbar = (Toolbar) ((AppCompatActivity) context).findViewById(R.id.toolbar);
        if (show) {
            toolbar.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(2));
        } else {
            toolbar.animate()
                    .translationY(-toolbar.getHeight())
                    .setInterpolator(new AccelerateInterpolator(2));
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}