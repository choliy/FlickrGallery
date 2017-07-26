package com.choliy.igor.flickrgallery.util;

import android.content.Context;
import android.text.format.DateFormat;

import com.choliy.igor.flickrgallery.FlickrConstants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class TimeUtils {

    private TimeUtils() {
    }

    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(FlickrConstants.DATE_FORMAT, Locale.ENGLISH);
        return sdf.format(new Date());
    }

    public static String getTime(Context context) {
        SimpleDateFormat sdf;
        if (DateFormat.is24HourFormat(context))
            sdf = new SimpleDateFormat(FlickrConstants.TIME_FORMAT_UK, Locale.UK);
        else
            sdf = new SimpleDateFormat(FlickrConstants.TIME_FORMAT_US, Locale.US);

        return sdf.format(new Date());
    }
}