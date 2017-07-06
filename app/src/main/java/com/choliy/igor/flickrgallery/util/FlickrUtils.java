package com.choliy.igor.flickrgallery.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.choliy.igor.flickrgallery.FlickrConstants;

public final class FlickrUtils {

    public static void showInfo(View view, String infoText) {
        Snackbar.make(view, infoText, Snackbar.LENGTH_SHORT).show();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static void hideKeyboard(Context context) {
        View view = ((AppCompatActivity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), FlickrConstants.NUMBER_ZERO);
            view.clearFocus();
        }
    }
}