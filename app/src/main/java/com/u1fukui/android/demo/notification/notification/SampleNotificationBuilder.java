package com.u1fukui.android.demo.notification.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.app.TaskStackBuilder;

import com.u1fukui.android.demo.notification.R;
import com.u1fukui.android.demo.notification.ui.ActionActivity;
import com.u1fukui.android.demo.notification.ui.ResultActivity;

public class SampleNotificationBuilder {

    public static final String KEY_GROUP = "key,group";

    public static final String KEY_REPLY_TEXT = "key.reply_text";

    public static final String EXTRA_REPLY_NOTIFICATION_ID = "extra.reply_notification_id";

    private Context context;

    private NotificationCompat.Builder builder;

    private boolean isHeadsUp;

    public static NotificationCompat.Builder createBaseNotificationBuilder(Context context) {
        return new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                .setContentTitle("ContentTitle")
                .setContentText("ContentText")
                .setContentInfo("ContentInfo")
                .setColor(Color.RED)
                .setGroup(KEY_GROUP)
                .setAutoCancel(true);
    }

    public SampleNotificationBuilder(Context context) {
        this.context = context;
        this.builder = createBaseNotificationBuilder(context);
    }

    public SampleNotificationBuilder notificationStyle(NotificationStyle notificationStyle) {
        NotificationCompat.Style style = notificationStyle.createStyle(context);
        if (style != null) {
            builder.setStyle(style);
        }
        return this;
    }

    public SampleNotificationBuilder addAction(String actionText) {
        PendingIntent intent = createPendingIntent(context, ActionActivity.class);
        NotificationCompat.Action action = new NotificationCompat.Action(0, actionText, intent);
        builder.addAction(action);
        return this;
    }

    public SampleNotificationBuilder addDirectReplyAction(String actionText, int notificationId) {
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_REPLY_TEXT)
                .setLabel("Let's reply")
                .build();

        Intent intent = new Intent("direct_reply");
        intent.setComponent(new ComponentName(context, DirectReplyReceiver.class));
        intent.putExtra(EXTRA_REPLY_NOTIFICATION_ID, notificationId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(0, actionText, pendingIntent)
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true) // for Android Wear
                .build();

        builder.addAction(action);

        return this;
    }

    public SampleNotificationBuilder headsUp(boolean isHeadsUp) {
        this.isHeadsUp = isHeadsUp;
        return this;
    }

    public Notification build() {
        PendingIntent intent = createPendingIntent(context, ResultActivity.class);
        if (isHeadsUp) {
            // Ref.) https://developer.android.com/about/versions/android-5.0-changes.html#BehaviorNotifications
            //
            // Examples of conditions that may trigger heads-up notifications include:
            // - The user's activity is in fullscreen mode (the app uses fullScreenIntent)
            // - The notification has high priority and uses ringtones or vibrations
            builder.setFullScreenIntent(intent, true);
        } else {
            builder.setContentIntent(intent);
        }

        return builder.build();
    }

    private static PendingIntent createPendingIntent(Context context, Class activityClass) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(activityClass);
        stackBuilder.addNextIntent(new Intent(context, activityClass));
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
