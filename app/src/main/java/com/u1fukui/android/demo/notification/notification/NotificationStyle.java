package com.u1fukui.android.demo.notification.notification;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import com.u1fukui.android.demo.notification.R;


public enum NotificationStyle {

    STYLE_NO("NoStyle") {
        @Override
        NotificationCompat.Style createStyle(Context context) {
            return null;
        }
    },
    STYLE_BIG_TEXT("BigTextStyle") {
        @Override
        NotificationCompat.Style createStyle(Context context) {
            return new NotificationCompat.BigTextStyle()
                    .bigText("BigText")
                    .setBigContentTitle(BIG_CONTENT_TITLE)
                    .setSummaryText(SUMMARY_TEXT);
        }
    },
    STYLE_BIG_PICTURE("BigPictureStyle") {
        @Override
        NotificationCompat.Style createStyle(Context context) {
            return new NotificationCompat.BigPictureStyle()
                    .setBigContentTitle(BIG_CONTENT_TITLE)
                    .setSummaryText(SUMMARY_TEXT)
                    .bigPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.big_picture_sample));
        }
    },
    STYLE_INBOX("InboxStyle") {
        @Override
        NotificationCompat.Style createStyle(Context context) {
            NotificationCompat.InboxStyle style =
                    new NotificationCompat.InboxStyle()
                            .setBigContentTitle(BIG_CONTENT_TITLE)
                            .setSummaryText(SUMMARY_TEXT);

            for (int i = 0; i < 10; i++) {
                style.addLine("Line" + (i + 1));
            }
            return style;
        }
    },
    STYLE_MEDIA("MediaStyle") {
        @Override
        NotificationCompat.Style createStyle(Context context) {
            return null;
        }
    },
    STYLE_MESSAGING("MessagingStyle") {
        @Override
        NotificationCompat.Style createStyle(Context context) {
            NotificationCompat.MessagingStyle style = new NotificationCompat.MessagingStyle(
                    "Me")
                    .setConversationTitle("ConversationTitle");

            for (int i = 0; i < 10; i++) {
                style.addMessage("Message" + i, System.currentTimeMillis(), i % 2 == 0 ? "Friend" : null);
            }

            return style;
        }
    };


    private static final String BIG_CONTENT_TITLE = "BigContentTitle";

    private static final String SUMMARY_TEXT = "SummaryText";

    private String displayName;

    abstract NotificationCompat.Style createStyle(Context context);

    NotificationStyle(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static NotificationStyle fromDisplayName(String displayName) {
        for (NotificationStyle style : values()) {
            if (style.getDisplayName().equals(displayName)) {
                return style;
            }
        }
        return null;
    }
}
