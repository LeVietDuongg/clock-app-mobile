package com.example.donghocuatoi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button baothuc, bamgio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bamgio = findViewById(R.id.bamgio);
        baothuc = findViewById(R.id.baothuc);
        bamgio.setOnClickListener(v -> {
            Intent bamgiointent = new Intent(MainActivity.this, BamGioActivity.class);
            startActivity(bamgiointent);
        });
        baothuc.setOnClickListener(v -> {
            Intent baothucintent = new Intent(MainActivity.this, BaoThucActivity.class);
            startActivity(baothucintent);
        });
    }
}
