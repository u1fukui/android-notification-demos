package com.u1fukui.android.demo.notification;

import com.u1fukui.android.demo.notification.databinding.MainActivityBinding;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ClickEventHandler {

    private static final String STYLE_NO = "NoStyle";

    private static final String STYLE_BIG_TEXT = "BigTextStyle";

    private static final String STYLE_BIG_PICTURE = "BigPictureStyle";

    private static final String STYLE_MEDIA = "MediaStyle";

    private static final String STYLE_MESSAGING = "MessagingStyle";

    private MainActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        binding.setHandler(this);

        initSpinner();
        binding.notificationIdEditText.setSelection(binding.notificationIdEditText.getText().length());
    }

    private void initSpinner() {
        String[] array = {
                STYLE_NO,
                STYLE_BIG_TEXT,
                STYLE_BIG_PICTURE,
                STYLE_MEDIA,
                STYLE_MESSAGING
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_style);
        adapter.addAll(array);
        binding.spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_button:
                onClickShowNotificationButton();
                break;
        }
    }

    public void onClickShowNotificationButton() {
        String notificationId = binding.notificationIdEditText.getText().toString();
        try {
            int id = Integer.parseInt(notificationId);
            showNotification(id);
        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.notification_id) + "に数値を入力してください。", Toast.LENGTH_LONG).show();
        }
    }

    private void showNotification(int notificationId) {
        String item = (String) binding.spinner.getSelectedItem();
        switch (item) {
            case STYLE_NO:
                showDefaultNotification(notificationId);
                break;
            case STYLE_BIG_TEXT:
                break;
            case STYLE_BIG_PICTURE:
                break;
            case  STYLE_MEDIA:
                break;
            case STYLE_MESSAGING:
                break;
        }
    }

    private void showDefaultNotification(int notificationId) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");

        Intent resultIntent = new Intent(this, ResultActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ResultActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }
}
