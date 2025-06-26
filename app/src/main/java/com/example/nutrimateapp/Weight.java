package com.example.nutrimateapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Weight extends AppCompatActivity {

    private TextView currentWeightText, targetWeightText;
    private SeekBar currentWeightSeekBar, targetWeightSeekBar;
    private Button backButton, continueButton;

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

        // Set max and initial values
        currentWeightSeekBar.setMax(60); // 71kg to 131kg
        targetWeightSeekBar.setMax(60);
        currentWeightSeekBar.setProgress(3); // 71 + 3 = 74kg
        targetWeightSeekBar.setProgress(9); // 71 + 9 = 80kg

        currentWeightText.setText("74.0 kg");
        targetWeightText.setText("80.0 kg");

        // SeekBar listeners
        currentWeightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int weight = 71 + progress;
                currentWeightText.setText(weight + ".0 kg");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        targetWeightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int weight = 71 + progress;
                targetWeightText.setText(weight + ".0 kg");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // 🔙 Back Button Functionality
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(Weight.this, HeightActivity.class); // Replace with your actual previous activity
            startActivity(intent);
            finish();
        });

        // ▶️ Continue Button Functionality
        continueButton.setOnClickListener(view -> {
            int currentWeight = 71 + currentWeightSeekBar.getProgress();
            int targetWeight = 71 + targetWeightSeekBar.getProgress();

        });
    }
}
