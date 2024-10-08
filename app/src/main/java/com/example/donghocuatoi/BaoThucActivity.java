package com.example.donghocuatoi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.Manifest;

import java.util.Calendar;

public class BaoThucActivity extends AppCompatActivity {
    Button btnback;
    TimePicker alarmTimePicker;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bao_thuc);

        btnback = findViewById(R.id.btnback);
        alarmTimePicker = findViewById(R.id.alarmTimePicker);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager == null) {
            Toast.makeText(this, "Lỗi: Không thể khởi tạo AlarmManager", Toast.LENGTH_SHORT).show();
        }

        btnback.setOnClickListener(v -> {
            finish();
        });
    }

    public void onToggleClicked(View view) {
        ToggleButton mytoggleButton = (ToggleButton) view;
        long time;
        if (mytoggleButton.isChecked()) {
            // tao toast khi bat bao thuc
            Toast.makeText(this, "Bật báo thức", Toast.LENGTH_SHORT).show();
            // getting current time
            Calendar mycalendar = Calendar.getInstance();
            mycalendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
            mycalendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());
            // create a pending intent
            Intent intent = new Intent(this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            // tính thời gian báo thức ở dạng mili giây
            time = mycalendar.getTimeInMillis();
            // kiểm tra thời gian hiện tại của hệ thống có lớn hơn thời gian báo thức hay không
            if (System.currentTimeMillis() > time) {
                //chỉnh thời gian dựa trên am hay pm
                if (mycalendar.get(Calendar.AM_PM) == Calendar.AM) {
                    time += 1000 * 60 * 60 * 12;
                } else {
                    time += 1000 * 60 * 60 * 24;
                }
            }
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);
        } else {
            // tao toast khi tat bao thuc
            Toast.makeText(this, "Tắt báo thức", Toast.LENGTH_LONG).show();
            alarmManager.cancel(pendingIntent);
        }
    }

}
