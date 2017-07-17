package com.choliy.igor.flickrgallery.util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.SystemClock;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.NotificationService;
import com.choliy.igor.flickrgallery.R;
import com.choliy.igor.flickrgallery.activity.GalleryActivity;

public final class AlarmUtils {

    public static void setServiceAlarm(Context context, boolean startService) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(
                context,
                FlickrConstants.NUMBER_ZERO,
                NotificationService.newIntent(context),
                FlickrConstants.NUMBER_ZERO);

        if (startService) {
            alarmManager.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(),
                    AlarmManager.INTERVAL_HOUR,
                    pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    public static void showNotification(Context context, String contentText) {
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(contentText)
                .setDefaults(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_HIGH)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setContentIntent(getContentIntent(context))
                .setAutoCancel(Boolean.TRUE)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(FlickrConstants.NUMBER_ZERO, notification);
    }

    private static PendingIntent getContentIntent(Context context) {
        return PendingIntent.getActivity(context,
                FlickrConstants.NUMBER_ZERO,
                GalleryActivity.newIntent(context),
                FlickrConstants.NUMBER_ZERO);
    }
}