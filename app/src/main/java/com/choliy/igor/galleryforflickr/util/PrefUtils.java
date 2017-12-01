package com.choliy.igor.galleryforflickr.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.choliy.igor.galleryforflickr.R;
import com.choliy.igor.galleryforflickr.tool.Constants;

public final class PrefUtils {

    private PrefUtils() {}

    public static String getStoredQuery(Context context) {
        return getPreferences(context).getString(context.getString(R.string.pref_key_search), Constants.EMPTY);
    }

    public static void setStoredQuery(Context context, String query) {
        getPreferences(context)
                .edit()
                .putString(context.getString(R.string.pref_key_search), query)
                .apply();
    }

    public static String getLastResultId(Context context) {
        return getPreferences(context).getString(context.getString(R.string.pref_key_last_id), Constants.EMPTY);
    }

    public static void setLastResultId(Context context, String lastResultId) {
        getPreferences(context)
                .edit()
                .putString(context.getString(R.string.pref_key_last_id), lastResultId)
                .apply();
    }

    public static String[] getSettings(Context context) {
        String gridValue = getGridSettings(context);
        String styleValue = getStyleSettings(context);
        boolean animationValue = getAnimationSettings(context);

        return new String[]{
                gridValue,
                styleValue,
                String.valueOf(animationValue)};
    }

    public static String getGridSettings(Context context) {
        return getPreferences(context).getString(context.getString(R.string.pref_key_grid), context.getString(R.string.pref_grid_size_value_3));
    }

    public static String getStyleSettings(Context context) {
        return getPreferences(context).getString(context.getString(R.string.pref_key_style), context.getString(R.string.pref_grid_style_value_1));
    }

    public static boolean getAnimationSettings(Context context) {
        return getPreferences(context).getBoolean(context.getString(R.string.pref_key_animation), Boolean.TRUE);
    }

    public static boolean getSplashSettings(Context context) {
        return getPreferences(context).getBoolean(context.getString(R.string.pref_key_splash), Boolean.TRUE);
    }

    public static String getPictureSettings(Context context) {
        return getPreferences(context).getString(context.getString(R.string.pref_key_picture), context.getString(R.string.pref_picture_value_200));
    }

    public static boolean getNotificationSettings(Context context) {
        return getPreferences(context).getBoolean(context.getString(R.string.pref_key_notification), Boolean.FALSE);
    }

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}