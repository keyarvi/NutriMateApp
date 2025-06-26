package com.example.nutrimateapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HeightActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);

        TextView heightText = findViewById(R.id.heightValue);
        SeekBar heightSeekBar = findViewById(R.id.heightSeekBar);
        Button backButton = findViewById(R.id.backButton);
        Button continueButton = findViewById(R.id.continueButton);

        // SeekBar listener
        heightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int height = 140 + progress; // Assuming 140cm as minimum
                heightText.setText(String.valueOf(height));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // No action needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // No action needed
            }
        });

        // 🔙 Back button to PrimaryGoal activity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HeightActivity.this, PrimaryGoal.class);
                startActivity(intent);
                finish();
            }
        });

        // ▶️ Continue button to Weight activity
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HeightActivity.this, Weight.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
