package com.u1fukui.android.demo.notification;

import java.util.ArrayList;
import java.util.List;

public enum NotificationStyle {
    STYLE_NO("NoStyle"),
    STYLE_BIG_TEXT("BigTextStyle"),
    STYLE_BIG_PICTURE("BigPictureStyle"),
    STYLE_INBOX("InboxStyle"),
    STYLE_MEDIA("MediaStyle"),
    STYLE_MESSAGING("MessagingStyle");

    private String displayName;

    NotificationStyle(String displayName) {
        this.displayName = displayName;
    }

    public static List<String> createStyleNameList() {
        List<String> list = new ArrayList<>();
        for (NotificationStyle style : values()) {
            list.add(style.displayName);
        }
        return list;
    }

    public static NotificationStyle fromDisplayName(String displayName) {
        for (NotificationStyle style : values()) {
            if (style.displayName.equals(displayName)) {
                return style;
            }
        }
        return null;
    }
}
