package com.u1fukui.android.demo.notification;

import com.u1fukui.android.demo.notification.databinding.MainActivityBinding;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ClickEventHandler {

    private static final int SUMMARY_NOTIFICATION_ID = 999;

    private MainActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        binding.setHandler(this);

        initStyleSpinner();
        initGroupingSwitch();
        binding.notificationIdEditText.setSelection(binding.notificationIdEditText.getText().length());
    }

    private void initStyleSpinner() {
        List<String> list = new ArrayList<>();
        for (NotificationStyle style : NotificationStyle.values()) {
            list.add(style.getDisplayName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_style);
        adapter.addAll(list);
        binding.spinner.setAdapter(adapter);
    }

    private void initGroupingSwitch() {
        binding.groupingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    compoundButton.setChecked(false);
                    Toast.makeText(MainActivity.this, "Nougat未満のバージョンでは利用できません", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

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
        String item = (String) binding.spinner.getSelectedItem();
        NotificationStyle style = NotificationStyle.fromDisplayName(item);
        Notification notification = NotificationCreator.createNotification(this, style, binding.headsUpSwitch.isChecked());

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (binding.groupingSwitch.isChecked() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationManager.notify(SUMMARY_NOTIFICATION_ID, NotificationCreator.createSummaryNotification(this));
        }
        notificationManager.notify(notificationId, notification);
    }
}
