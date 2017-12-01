package com.choliy.igor.galleryforflickr.async;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.choliy.igor.galleryforflickr.util.AlarmUtils;
import com.choliy.igor.galleryforflickr.util.PrefUtils;

public class StartupReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmUtils.setServiceAlarm(
                context.getApplicationContext(),
                PrefUtils.getNotificationSettings(context));
    }
}