package com.example.nutrimateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Weight extends AppCompatActivity {

    private TextView currentWeightText, targetWeightText;
    private SeekBar currentWeightSeekBar, targetWeightSeekBar;
    private Button backButton, continueButton;

    private final int minWeight = 71;  // Minimum weight represented by SeekBars

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        // Initialize views
        currentWeightText = findViewById(R.id.currentWeightText);
        targetWeightText = findViewById(R.id.targetWeightText);
        currentWeightSeekBar = findViewById(R.id.currentWeightSeekBar);
        targetWeightSeekBar = findViewById(R.id.targetWeightSeekBar);
        backButton = findViewById(R.id.backButton);
        continueButton = findViewById(R.id.continueButton);

        // Set max values
        currentWeightSeekBar.setMax(60); // 71kg to 131kg
        targetWeightSeekBar.setMax(60);

        // Set default progress
        currentWeightSeekBar.setProgress(3); // 74kg
        targetWeightSeekBar.setProgress(9);  // 80kg

        // Display initial values
        currentWeightText.setText((minWeight + currentWeightSeekBar.getProgress()) + ".0 kg");
        targetWeightText.setText((minWeight + targetWeightSeekBar.getProgress()) + ".0 kg");

        // Listeners
        currentWeightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentWeightText.setText((minWeight + progress) + ".0 kg");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        targetWeightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                targetWeightText.setText((minWeight + progress) + ".0 kg");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // 🔙 Back to HeightActivity
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(Weight.this, HeightActivity.class);
            startActivity(intent);
            finish();
        });

        // ▶️ Continue to Complete
        continueButton.setOnClickListener(view -> {
            int currentWeight = minWeight + currentWeightSeekBar.getProgress();
            int targetWeight = minWeight + targetWeightSeekBar.getProgress();

            // Save to SharedPreferences
            SharedPreferences prefs = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("USER_CURRENT_WEIGHT", currentWeight);
            editor.putInt("USER_TARGET_WEIGHT", targetWeight);
            editor.apply();

            // Proceed to Complete
            Intent intent = new Intent(Weight.this, LifestylePace.class);
            startActivity(intent);
            finish();
        });
    }
}
