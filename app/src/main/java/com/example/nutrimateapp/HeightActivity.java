package com.example.nutrimateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HeightActivity extends AppCompatActivity {

    private int selectedHeight = 180; // Default height
    private TextView heightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);

        heightText = findViewById(R.id.heightValue);
        SeekBar heightSeekBar = findViewById(R.id.heightSeekBar);
        Button backButton = findViewById(R.id.backButton);
        Button continueButton = findViewById(R.id.continueButton);

        // Default height shown
        heightText.setText(String.valueOf(selectedHeight));

        // SeekBar listener
        heightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedHeight = 140 + progress; // Range: 140cm to 200cm
                heightText.setText(String.valueOf(selectedHeight));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // 🔙 Back button to PrimaryGoal activity
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(HeightActivity.this, Weight.class);
            startActivity(intent);
            finish();
        });

        // ▶️ Continue button to Weight activity
        continueButton.setOnClickListener(view -> {
            // Save selected height in cm to SharedPreferences
            SharedPreferences prefs = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE);
            prefs.edit().putInt("USER_HEIGHT", selectedHeight).apply();

            Intent intent = new Intent(HeightActivity.this, Weight.class);
            startActivity(intent);
            finish();
        });
    }
}
