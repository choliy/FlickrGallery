package com.choliy.igor.flickrgallery.async;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.choliy.igor.flickrgallery.interfaces.FlickrConstants;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (getResultCode() == Activity.RESULT_CANCELED) return;

        Notification notification = intent.getParcelableExtra(FlickrConstants.NOTIFICATION_KEY);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(FlickrConstants.INT_ZERO, notification);
    }
}