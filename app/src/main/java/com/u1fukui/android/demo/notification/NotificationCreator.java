package com.u1fukui.android.demo.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class NotificationCreator {

    private static String GROUP_KEY = "group_key";

    public static Notification createNotification(Context context, NotificationStyle notificationStyle) {
        Intent resultIntent = new Intent(context, ResultActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ResultActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("ContentTitle")
                        .setContentText("ContentText")
                        .setContentIntent(resultPendingIntent)
                        .setGroup(GROUP_KEY);

        NotificationCompat.Style style = notificationStyle.createStyle(context);
        if (style != null) {
            builder.setStyle(style);
        }
        return builder.build();
    }

    public static Notification createSummaryNotification(Context context) {
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setSummaryText("SummaryText"))
                .setGroup(GROUP_KEY)
                .setGroupSummary(true)
                .build();
    }
}
