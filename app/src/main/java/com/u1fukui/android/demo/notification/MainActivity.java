package com.u1fukui.android.demo.notification;

import com.u1fukui.android.demo.notification.databinding.MainActivityBinding;

import android.app.Notification;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ClickEventHandler {

    private static final int GROUP_SUMMARY_NOTIFICATION_ID = 9999; // 他の Notification ID と被らないように

    private MainActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        binding.setHandler(this);

        initStyleSpinner();
        initButtonCountSpinner();
        initDirectReplySwitch();
        initGroupingSwitch();
        binding.notificationIdEditText.setSelection(binding.notificationIdEditText.getText().length());
    }

    //region Initialize
    private void initStyleSpinner() {
        List<String> list = new ArrayList<>();
        for (NotificationStyle style : NotificationStyle.values()) {
            list.add(style.getDisplayName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_style);
        adapter.addAll(list);
        binding.styleSpinner.setAdapter(adapter);
    }

    private void initButtonCountSpinner() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_style);
        adapter.addAll(list);
        binding.buttonCountSpinner.setAdapter(adapter);
    }

    private void initGroupingSwitch() {
        binding.groupingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    Toast.makeText(MainActivity.this, "Nougat未満のバージョンでは利用できません", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void initDirectReplySwitch() {
        binding.directReplySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    Toast.makeText(MainActivity.this, "Nougat未満のバージョンでは利用できません", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
    //endregion

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_button:
                onClickShowNotificationButton();
                break;
        }
    }

    private void onClickShowNotificationButton() {
        try {
            int notificationId = Integer.parseInt(binding.notificationIdEditText.getText().toString());
            showNotification(notificationId);
        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.notification_id) + "に数値を入力してください。", Toast.LENGTH_LONG).show();
        }
    }

    private void showNotification(int notificationId) {
        NotificationStyle style = NotificationStyle.fromDisplayName((String) binding.styleSpinner.getSelectedItem());
        int buttonCount = Integer.parseInt((String) binding.buttonCountSpinner.getSelectedItem());
        boolean isDirectReply = binding.directReplySwitch.isChecked();
        boolean isHeadsUp = binding.headsUpSwitch.isChecked();
        boolean isGrouping = binding.groupingSwitch.isChecked();

        // Grouping
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (isGrouping && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationManager.notify(GROUP_SUMMARY_NOTIFICATION_ID, createGroupSummaryNotification());
        }

        // Notify
        SampleNotificationBuilder builder =
                new SampleNotificationBuilder(this)
                        .notificationStyle(style)
                        .headsUp(isHeadsUp);

        for (int i = 0; i < buttonCount; i++) {
            if (isDirectReply) {
                builder.addDirectReplyAction("Reply" + i);
            } else {
                builder.addAction("Action" + i);
            }
        }

        notificationManager.notify(notificationId, builder.build());
    }

    private Notification createGroupSummaryNotification() {
        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setSummaryText("SummaryText"))
                .setGroup(SampleNotificationBuilder.KEY_GROUP)
                .setGroupSummary(true)
                .build();
    }
}
