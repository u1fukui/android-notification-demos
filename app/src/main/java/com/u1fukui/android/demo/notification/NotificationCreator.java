package com.u1fukui.android.demo.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class NotificationCreator {

    private static String GROUP_KEY = "group_key";

    public static Notification createNotification(Context context,
            NotificationStyle notificationStyle, int actionButtonCount, boolean needHeadsUp) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.ic_popup_reminder)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                        .setContentTitle("ContentTitle")
                        .setContentText("ContentText")
                        .setContentInfo("ContentInfo")
                        .setColor(Color.RED)
                        .setGroup(GROUP_KEY);

        // Heads-up
        PendingIntent intent = createPendingIntent(context, ResultActivity.class);
        if (needHeadsUp) {
            // Ref.) https://developer.android.com/about/versions/android-5.0-changes.html#BehaviorNotifications
            //
            // Examples of conditions that may trigger heads-up notifications include:
            // - The user's activity is in fullscreen mode (the app uses fullScreenIntent)
            // - The notification has high priority and uses ringtones or vibrations
            builder.setFullScreenIntent(intent, true);
        } else {
            builder.setContentIntent(intent);
        }

        // Style
        NotificationCompat.Style style = notificationStyle.createStyle(context);
        if (style != null) {
            builder.setStyle(style);
        }

        // Action button
        for (int i = 0; i < actionButtonCount; i++) {
            builder.addAction(createAction(context, i));
        }

        return builder.build();
    }

    private static PendingIntent createPendingIntent(Context context, Class activityClass) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(activityClass);
        stackBuilder.addNextIntent(new Intent(context, activityClass));
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static NotificationCompat.Action createAction(Context context, int position) {
        PendingIntent intent = createPendingIntent(context, ActionActivity.class);
        return new NotificationCompat.Action(0, "Action" + position, intent);
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
