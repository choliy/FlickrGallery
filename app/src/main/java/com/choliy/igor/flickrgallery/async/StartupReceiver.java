package com.choliy.igor.flickrgallery.async;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.choliy.igor.flickrgallery.util.AlarmUtils;
import com.choliy.igor.flickrgallery.util.PrefUtils;

public class StartupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmUtils.setServiceAlarm(
                context.getApplicationContext(),
                PrefUtils.getNotificationSettings(context));
    }
}