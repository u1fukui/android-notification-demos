package com.u1fukui.android.demo.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class NotificationCreator {

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
                        .setContentIntent(resultPendingIntent);

        NotificationCompat.Style style = notificationStyle.createStyle(context);
        if (style != null) {
            builder.setStyle(style);
        }
        return builder.build();
    }
}
