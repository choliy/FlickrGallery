package com.choliy.igor.flickrgallery.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;

public final class PrefUtils {

    public static void isFirstStart(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean previouslyStarted = preferences.getBoolean(
                context.getString(R.string.pref_key_first_start), Boolean.FALSE);
        if (!previouslyStarted) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean(context.getString(R.string.pref_key_first_start), Boolean.TRUE);
            edit.putString(context.getString(R.string.pref_key_grid), FlickrConstants.DEFAULT_GRID);
            edit.putString(context.getString(R.string.pref_key_picture), FlickrConstants.DEFAULT_PICTURE);
            edit.putBoolean(context.getString(R.string.pref_key_animation), FlickrConstants.DEFAULT_ANIMATION);
            edit.apply();
        }
    }

    public static String getStoredQuery(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(FlickrConstants.PREF_SEARCH_QUERY, null);
    }

    public static void setStoredQuery(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(FlickrConstants.PREF_SEARCH_QUERY, query)
                .apply();
    }

    public static String[] getSettings(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String gridValue = preferences.getString(context.getString(R.string.pref_key_grid), null);
        String pictureValue = preferences.getString(context.getString(R.string.pref_key_picture), null);
        boolean animationValue = preferences.getBoolean(context.getString(R.string.pref_key_animation), Boolean.FALSE);

        return new String[]{gridValue, pictureValue, String.valueOf(animationValue)};
    }

    public static String getGridSettings(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.pref_key_grid), null);
    }

    public static String getPictureSettings(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.pref_key_picture), null);
    }

    public static boolean getAnimationSettings(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getString(R.string.pref_key_animation), Boolean.FALSE);
    }
}