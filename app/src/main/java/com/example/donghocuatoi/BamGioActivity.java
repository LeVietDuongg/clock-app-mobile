package com.example.donghocuatoi;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BamGioActivity extends AppCompatActivity {

    private TextView timerText;
    private Button startButton, stopAlarmButton, btnback;
    private NumberPicker minutePicker, secondPicker;
    private CountDownTimer countDownTimer;
    private boolean timerRunning = false;
    private boolean timerPaused = false; // Biến lưu trạng thái tạm dừng
    private long timeLeftInMillis; // Thời gian đếm ngược theo milliseconds
    private long initialTimeInMillis; // Thời gian ban đầu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bam_gio);

        // Khởi tạo các view
        timerText = findViewById(R.id.timer_text);
        startButton = findViewById(R.id.start_button);
        stopAlarmButton = findViewById(R.id.stop_alarm_button); // Nút dừng chuông
        btnback = findViewById(R.id.btnback);
        minutePicker = findViewById(R.id.minute_picker);
        secondPicker = findViewById(R.id.second_picker);

        // Thiết lập giá trị min và max cho NumberPicker
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(59);

        // Khi nhấn nút start
        startButton.setOnClickListener(view -> {
            if (!timerRunning) {
                if (!timerPaused) {
                    // Lấy thời gian từ NumberPicker lần đầu tiên
                    int minutes = minutePicker.getValue();
                    int seconds = secondPicker.getValue();
                    initialTimeInMillis = (minutes * 60 + seconds) * 1000; // Chuyển thành milliseconds
                    timeLeftInMillis = initialTimeInMillis; // Gán thời gian ban đầu vào biến lưu thời gian còn lại
                }

                if (timeLeftInMillis > 0) {
                    startTimer(); // Bắt đầu đếm ngược
                } else {
                    Toast.makeText(this, "Vui lòng chọn thời gian hợp lệ", Toast.LENGTH_SHORT).show();
                }
            } else {
                pauseTimer(); // Tạm dừng đếm ngược
            }
        });

        // Quay lại home
        btnback.setOnClickListener(v -> finish());

        // Dừng chuông
        stopAlarmButton.setOnClickListener(v -> stopAlarm());
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished; // Cập nhật thời gian còn lại
                updateTimer(); // Hiển thị thời gian
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                timerPaused = false; // Reset trạng thái tạm dừng
                startButton.setText("Start Timer");
                Toast.makeText(BamGioActivity.this, "Đã hết thời gian!", Toast.LENGTH_SHORT).show();

                // Phát âm báo khi hết thời gian
                playAlarm();
            }
        }.start();

        timerRunning = true;
        timerPaused = false; // Khi đếm ngược bắt đầu, trạng thái tạm dừng là false
        startButton.setText("Pause Timer"); // Đổi text của nút thành "Tạm dừng"
    }

    private void pauseTimer() {
        countDownTimer.cancel(); // Hủy bộ đếm ngược
        timerRunning = false;
        timerPaused = true; // Đặt trạng thái tạm dừng

        startButton.setText("Resume Timer"); // Đổi text của nút thành "Tiếp tục"
    }

    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timerText.setText(timeLeftFormatted); // Cập nhật giao diện với thời gian mới
    }

    private void playAlarm() {
        // Hiển thị nút Dừng chuông
        stopAlarmButton.setVisibility(Button.VISIBLE);

        // Sử dụng Intent để kích hoạt AlarmReceiver và phát âm thanh báo thức
        Intent intent = new Intent(this, AlarmReceiver.class);
        sendBroadcast(intent);
    }

    private void stopAlarm() {
        // Gửi Intent để dừng âm báo
        Intent stopIntent = new Intent(this, AlarmReceiver.class);
        stopIntent.setAction("STOP_ALARM");
        sendBroadcast(stopIntent);

        // Ẩn nút Dừng chuông
        stopAlarmButton.setVisibility(Button.GONE);
    }
}
