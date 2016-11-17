package com.u1fukui.android.demo.notification.notification;

import com.u1fukui.android.demo.notification.R;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * Ref.) https://www.binpress.com/tutorial/using-android-media-style-notifications-with-media-session-controls/165
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MediaPlayerService extends Service {

    private static final String TAG = MediaPlayerService.class.getSimpleName();

    private static final String EXTRA_NOTIFICATION_ID = "extra.notification_id";

    public static final String ACTION_PLAY = "action.play";

    public static final String ACTION_PAUSE = "action.pause";

    public static final String ACTION_REWIND = "action.rewind";

    public static final String ACTION_FAST_FORWARD = "action.fast_foward";

    public static final String ACTION_NEXT = "action.next";

    public static final String ACTION_PREVIOUS = "action.previous";

    public static final String ACTION_STOP = "action.stop";

    private MediaSession mediaSession;

    private MediaController mediaController;

    public static void startService(Context context, int notificationId) {
        context.startService(createIntent(context, notificationId, ACTION_PLAY));
    }

    public static Intent createIntent(Context context, int notificationId, String action) {
        Intent intent = new Intent(context, MediaPlayerService.class);
        intent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
        intent.setAction(action);
        return intent;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1);
        Log.d(TAG, "notificationId=" + notificationId);
        initMediaSessions(notificationId);
        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void initMediaSessions(int notificationId) {
        mediaSession = new MediaSession(this, "simple player session");
        mediaController = new MediaController(this, mediaSession.getSessionToken());
        mediaSession.setCallback(new MediaSessionCallback(this, notificationId));
    }

    private void handleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }

        String action = intent.getAction();
        switch (action) {
            case ACTION_PLAY:
                mediaController.getTransportControls().play();
                break;
            case ACTION_PAUSE:
                mediaController.getTransportControls().pause();
                break;
            case ACTION_FAST_FORWARD:
                mediaController.getTransportControls().fastForward();
                break;
            case ACTION_REWIND:
                mediaController.getTransportControls().rewind();
                break;
            case ACTION_PREVIOUS:
                mediaController.getTransportControls().skipToPrevious();
                break;
            case ACTION_NEXT:
                mediaController.getTransportControls().skipToNext();
                break;
            case ACTION_STOP:
                mediaController.getTransportControls().stop();
                break;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaSession.release();
        return super.onUnbind(intent);
    }

    private static class MediaSessionCallback extends MediaSession.Callback {

        private Context context;

        private int notificationId;

        public MediaSessionCallback(Context context, int notificationId) {
            this.context = context;
            this.notificationId = notificationId;
        }

        @Override
        public void onPlay() {
            super.onPlay();
            Log.e(TAG, "onPlay");
            updateNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
        }

        @Override
        public void onPause() {
            super.onPause();
            Log.e(TAG, "onPause");
            updateNotification(generateAction(android.R.drawable.ic_media_play, "Play", ACTION_PLAY));
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
            Log.e(TAG, "onSkipToNext");
            //Change media here
            updateNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            Log.e(TAG, "onSkipToPrevious");
            //Change media here
            updateNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
        }

        @Override
        public void onFastForward() {
            super.onFastForward();
            Log.e(TAG, "onFastForward");
            //Manipulate current media here
        }

        @Override
        public void onRewind() {
            super.onRewind();
            Log.e(TAG, "onRewind");
            //Manipulate current media here
        }

        @Override
        public void onStop() {
            super.onStop();
            Log.e(TAG, "onStop");
            //Stop media player here
            NotificationManagerCompat.from(context).cancel(notificationId);
            context.stopService(new Intent(context, MediaPlayerService.class));
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
        }

        @Override
        public void onSetRating(Rating rating) {
            super.onSetRating(rating);
        }

        private void updateNotification(NotificationCompat.Action action) {
            Intent intent = MediaPlayerService.createIntent(context, notificationId, ACTION_STOP);
            PendingIntent pendingIntent = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setContentTitle("Media Title");
            builder.setContentText("Media Artist");
            builder.setDeleteIntent(pendingIntent);

            NotificationCompat.MediaStyle style = new NotificationCompat.MediaStyle();
            builder.setStyle(style);
            builder.addAction(generateAction(android.R.drawable.ic_media_previous, "Previous", ACTION_PREVIOUS));
            builder.addAction(generateAction(android.R.drawable.ic_media_rew, "Rewind", ACTION_REWIND));
            builder.addAction(action);
            builder.addAction(generateAction(android.R.drawable.ic_media_ff, "Fast Foward", ACTION_FAST_FORWARD));
            builder.addAction(generateAction(android.R.drawable.ic_media_next, "Next", ACTION_NEXT));
            style.setShowActionsInCompactView(0, 1, 2, 3, 4);

            NotificationManagerCompat.from(context).notify(notificationId, builder.build());
        }

        private NotificationCompat.Action generateAction(int icon, String title, String intentAction) {
            Intent intent = MediaPlayerService.createIntent(context, notificationId, intentAction);
            PendingIntent pendingIntent = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            return new NotificationCompat.Action.Builder(icon, title, pendingIntent).build();
        }
    }
}
