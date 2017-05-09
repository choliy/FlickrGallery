package com.choliy.igor.flickrgallery.util;

import android.content.Context;
import android.preference.PreferenceManager;

import com.choliy.igor.flickrgallery.FlickrConstants;

public final class PreferenceUtils {

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
}