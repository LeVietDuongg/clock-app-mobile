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
            // tạo toast khi bật báo thức
            Toast.makeText(this, "Bật báo thức", Toast.LENGTH_SHORT).show();

            // lấy thời gian hiện tại
            Calendar mycalendar = Calendar.getInstance();
            mycalendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
            mycalendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());

            // tạo một intent để kích hoạt BroadcastReceiver (AlarmReceiver)
            Intent intent = new Intent(this, AlarmReceiver.class);

            // Tạo requestCode duy nhất dựa trên thời gian của báo thức
            int requestCode = (int) mycalendar.getTimeInMillis();

            // Tạo một PendingIntent với requestCode duy nhất
            pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);

            // tính thời gian báo thức ở dạng mili giây
            time = mycalendar.getTimeInMillis();

            // kiểm tra thời gian hiện tại của hệ thống có lớn hơn thời gian báo thức hay không
            if (System.currentTimeMillis() > time) {
                // nếu báo thức đã qua, đặt lại vào hôm sau
                time += 1000 * 60 * 60 * 24;
            }

            // đặt báo thức lặp lại
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            // Tắt báo thức với requestCode duy nhất
            Toast.makeText(this, "Tắt báo thức", Toast.LENGTH_LONG).show();

            // Tìm requestCode duy nhất từ thời gian báo thức
            Calendar mycalendar = Calendar.getInstance();
            mycalendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
            mycalendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());
            int requestCode = (int) mycalendar.getTimeInMillis();

            // Hủy bỏ báo thức
            pendingIntent = PendingIntent.getBroadcast(this, requestCode, new Intent(this, AlarmReceiver.class), PendingIntent.FLAG_IMMUTABLE);
            alarmManager.cancel(pendingIntent);
        }
    }


}
