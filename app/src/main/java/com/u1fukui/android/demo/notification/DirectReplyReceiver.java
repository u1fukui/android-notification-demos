package com.u1fukui.android.demo.notification;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;

public class DirectReplyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        CharSequence messageText = getMessageText(intent);

        Notification repliedNotification =
                SampleNotificationBuilder.createBaseNotificationBuilder(context)
                        .setContentTitle(messageText)
                        .setContentText(null)
                        .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, repliedNotification);
    }

    private CharSequence getMessageText(Intent intent) {
        Bundle results = RemoteInput.getResultsFromIntent(intent);
        if (results != null) {
            return results.getCharSequence(SampleNotificationBuilder.KEY_REPLY_TEXT);
        }
        return null;
    }
}
