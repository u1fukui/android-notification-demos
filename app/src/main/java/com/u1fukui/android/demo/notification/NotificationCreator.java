package com.u1fukui.android.demo.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class NotificationCreator {

    private static final String BIG_CONTENT_TITLE = "BigContentTitle";

    private static final String SUMMARY_TEXT = "SummaryText";

    private Context context;

    public NotificationCreator(Context context) {
        this.context = context;
    }

    public Notification createNotification(NotificationStyle notificationStyle) {
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

        NotificationCompat.Style style = createNotificationStyle(notificationStyle);
        if (style != null) {
            builder.setStyle(style);
        }
        return builder.build();
    }

    private NotificationCompat.Style createNotificationStyle(NotificationStyle notificationStyle) {
        switch (notificationStyle) {
            case STYLE_BIG_TEXT:
                return createBigTextStyle();
            case STYLE_BIG_PICTURE:
                return createBigPictureStyle();
            case STYLE_INBOX:
                return createInboxStyle();
            case STYLE_MEDIA:
                //TODO: 実装
                return null;
            case STYLE_MESSAGING:
                //TODO: 実装
                return null;
            case STYLE_NO:
            default:
                return null;
        }
    }

    private NotificationCompat.Style createBigTextStyle() {
        return new NotificationCompat.BigTextStyle()
                .bigText("BigText")
                .setBigContentTitle(BIG_CONTENT_TITLE)
                .setSummaryText(SUMMARY_TEXT);
    }

    private NotificationCompat.Style createBigPictureStyle() {
        return new NotificationCompat.BigPictureStyle()
                .setBigContentTitle(BIG_CONTENT_TITLE)
                .setSummaryText(SUMMARY_TEXT)
                .bigPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.big_picture_sample))
                .bigLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.big_picture_sample));
    }

    private NotificationCompat.Style createInboxStyle() {
        NotificationCompat.InboxStyle style =
                new NotificationCompat.InboxStyle()
                        .setBigContentTitle(BIG_CONTENT_TITLE)
                        .setSummaryText(SUMMARY_TEXT);

        for (int i = 0; i < 10; i++) {
            style.addLine("Line" + (i + 1));
        }
        return style;
    }
}
