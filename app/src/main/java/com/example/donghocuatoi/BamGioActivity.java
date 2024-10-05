package com.example.donghocuatoi;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BamGioActivity extends AppCompatActivity {

    private TextView timerText;
    private Button startButton, btnback;
    private CountDownTimer countDownTimer;
    private boolean timerRunning = false;
    private long timeLeftInMillis = 60000; // 1 minute in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bam_gio);

        // Initialize the views
        timerText = findViewById(R.id.timer_text);
        startButton = findViewById(R.id.start_button);
        btnback = findViewById(R.id.btnback);
        // Set an OnClickListener for the start button
        startButton.setOnClickListener(view -> {
            Toast.makeText(this, "Bắt đầu đếm ngược", Toast.LENGTH_SHORT).show();
            if (!timerRunning) {
                startTimer();
            }
        });
        btnback.setOnClickListener(v -> {
            finish();
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                startButton.setText("Start Timer");
            }
        }.start();

        timerRunning = true;
        startButton.setText("Pause Timer");
    }

    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timerText.setText(timeLeftFormatted);
    }
}
