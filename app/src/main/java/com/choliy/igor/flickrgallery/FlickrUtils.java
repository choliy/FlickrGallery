package com.choliy.igor.flickrgallery;

import android.support.design.widget.Snackbar;
import android.view.View;

public final class FlickrUtils {

    public static void showInfo(View view, String infoText) {
        Snackbar.make(view, infoText, Snackbar.LENGTH_SHORT).show();
    }

    public static void showLongInfo(View view, String infoText) {
        Snackbar.make(view, infoText, Snackbar.LENGTH_LONG).show();
    }
}