package com.example.donghocuatoi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class AlarmReceiver extends BroadcastReceiver {
    private static Ringtone ringtone; // Static để duy trì trạng thái của ringtone giữa các lần phát

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onReceive(Context context, Intent intent) {
        // Kiểm tra xem có phải yêu cầu dừng chuông không
        if (intent.getAction() != null && intent.getAction().equals("STOP_ALARM")) {
            stopAlarm(); // Gọi phương thức dừng chuông
            return; // Thoát nếu nhận lệnh dừng chuông
        }

        // Sử dụng vibrator để rung thiết bị
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(4000);  // Rung trong 4 giây
        }

        // Hiển thị thông báo Toast
        Toast.makeText(context, "Alarm! Wake up! Wake up!", Toast.LENGTH_LONG).show();

        // Lấy URI âm báo
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        // Phát âm báo
        if (ringtone == null) {
            ringtone = RingtoneManager.getRingtone(context, alarmUri);
        }

        if (ringtone != null && !ringtone.isPlaying()) {
            ringtone.play();
        }
    }

    // Hàm dừng âm báo
    public void stopAlarm() {
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
            ringtone = null; // Giải phóng ringtone sau khi dừng
        }
    }
}
