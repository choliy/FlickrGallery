package net.wizapps.fgallery.async;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.wizapps.fgallery.util.AlarmUtils;
import net.wizapps.fgallery.util.PrefUtils;

public class StartupReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmUtils.setServiceAlarm(
                context.getApplicationContext(),
                PrefUtils.getNotificationSettings(context));
    }
}