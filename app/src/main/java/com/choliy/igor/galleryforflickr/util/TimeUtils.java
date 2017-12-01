package com.choliy.igor.galleryforflickr.util;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import com.choliy.igor.galleryforflickr.tool.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class TimeUtils {

    private TimeUtils() {}

    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH);
        return sdf.format(new Date());
    }

    public static String getTime(Context context) {
        SimpleDateFormat sdf;
        if (DateFormat.is24HourFormat(context)) {
            sdf = new SimpleDateFormat(Constants.TIME_FORMAT_UK, Locale.UK);
        } else {
            sdf = new SimpleDateFormat(Constants.TIME_FORMAT_US, Locale.US);
        }

        return sdf.format(new Date());
    }

    public static String formatDate(Context context, String dateTaken) {
        Date date;
        String formattedDate;
        String formatTaken = Constants.TIME_FORMAT_DATE_TAKEN;
        String formatUk = Constants.INFO_DATE_FORMAT_UK;
        String formatUs = Constants.INFO_DATE_FORMAT_US;
        try {
            if (DateFormat.is24HourFormat(context)) {
                date = new SimpleDateFormat(formatTaken, Locale.UK).parse(dateTaken);
                formattedDate = new SimpleDateFormat(formatUk, Locale.UK).format(date);
            } else {
                date = new SimpleDateFormat(formatTaken, Locale.US).parse(dateTaken);
                formattedDate = new SimpleDateFormat(formatUs, Locale.US).format(date);
            }
            return formattedDate;
        } catch (ParseException e) {
            Log.e(TimeUtils.class.getSimpleName(), e.getMessage());
        }
        return null;
    }
}