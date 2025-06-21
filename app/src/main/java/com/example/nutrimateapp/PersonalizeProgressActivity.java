package com.example.nutrimateapp;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PersonalizeProgressActivity extends AppCompatActivity {

    private TextView currentWeightText, targetWeightText;
    private SeekBar currentWeightSeekBar, targetWeightSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalize_progress); // Link to your XML layout

        // Initialize views
        currentWeightText = findViewById(R.id.currentWeightText);
        targetWeightText = findViewById(R.id.targetWeightText);
        currentWeightSeekBar = findViewById(R.id.currentWeightSeekBar);
        targetWeightSeekBar = findViewById(R.id.targetWeightSeekBar);

        // Setup SeekBars
        currentWeightSeekBar.setMax(60); // e.g., range 71-131 kg
        targetWeightSeekBar.setMax(60);

        currentWeightSeekBar.setProgress(3); // corresponds to 74kg (71 + 3)
        targetWeightSeekBar.setProgress(9); // corresponds to 80kg (71 + 9)

        currentWeightText.setText("74.0 kg");
        targetWeightText.setText("80.0 kg");

        currentWeightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int weight = 71 + progress;
                currentWeightText.setText(weight + ".0 kg");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        targetWeightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int weight = 71 + progress;
                targetWeightText.setText(weight + ".0 kg");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
}
