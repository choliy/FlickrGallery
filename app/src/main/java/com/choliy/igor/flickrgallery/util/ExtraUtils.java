package com.choliy.igor.flickrgallery.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;

import java.util.Calendar;

public final class ExtraUtils {

    private ExtraUtils() {
    }

    public static void showInfo(View view, String infoText) {
        Snackbar.make(view, infoText, Snackbar.LENGTH_SHORT).show();
    }

    public static void showLongInfo(Context context, String infoText) {
        Toast.makeText(context, infoText, Toast.LENGTH_LONG).show();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        return isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
    }

    public static void hideKeyboard(Context context) {
        View view = ((AppCompatActivity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), FlickrConstants.INT_ZERO);
            view.clearFocus();
        }
    }

    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, Boolean.TRUE))
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        else
            return Math.round(context.getResources().getDimension(R.dimen.dimen_toolbar_height));
    }

    public static void setupDevDate(Context context, NavigationView view) {
        View header = view.getHeaderView(FlickrConstants.INT_ZERO);
        TextView developer = (TextView) header.findViewById(R.id.nav_text_developer);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String text = context.getString(R.string.dialog_developer, String.valueOf(year));
        developer.setText(text);
    }

    public static String parseDescription(Context context, String description) {
        String defaultDescription = context.getString(R.string.text_def_description);
        String emptyDescription = context.getString(R.string.text_empty_description);
        if (description.equals(defaultDescription)) {
            return emptyDescription;
        } else {
            description = description.replace(
                    context.getString(R.string.text_replace_chars),
                    FlickrConstants.STRING_EMPTY);
            description = description.substring(0, description.length() - 2);
            return description;
        }
    }
}