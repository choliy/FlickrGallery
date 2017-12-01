package com.choliy.igor.galleryforflickr.util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.choliy.igor.galleryforflickr.R;
import com.choliy.igor.galleryforflickr.activity.GalleryActivity;
import com.choliy.igor.galleryforflickr.async.NotificationService;
import com.choliy.igor.galleryforflickr.tool.Constants;

public final class AlarmUtils {

    private AlarmUtils() {}

    public static void setServiceAlarm(Context context, boolean startService) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(
                context,
                Constants.ZERO,
                NotificationService.newIntent(context),
                Constants.ZERO);

        if (startService) {
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(
                        AlarmManager.ELAPSED_REALTIME,
                        SystemClock.elapsedRealtime(),
                        AlarmManager.INTERVAL_HOUR,
                        pendingIntent);
            }
        } else {
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
            pendingIntent.cancel();
        }
    }

    public static Notification getNotification(Context context, String contentText) {
        String channelId = context.getPackageName();
        initChannel(context, channelId);
        return new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notifications_white)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(contentText)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_HIGH)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setContentIntent(getContentIntent(context))
                .setAutoCancel(Boolean.TRUE)
                .build();
    }

    private static void initChannel(Context context, String channelId) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            return;
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(
                channelId,
                AlarmUtils.class.getSimpleName(),
                NotificationManager.IMPORTANCE_HIGH);

        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }

    private static PendingIntent getContentIntent(Context context) {
        return PendingIntent.getActivity(context,
                Constants.ZERO,
                GalleryActivity.newIntent(context),
                Constants.ZERO);
    }
}