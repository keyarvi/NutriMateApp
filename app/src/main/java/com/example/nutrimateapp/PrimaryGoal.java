package com.example.nutrimateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PrimaryGoal extends AppCompatActivity {

    LinearLayout btnLoseWeight, btnGainMuscle, btnMaintainWeight;
    Button backButton, continueButton;
    String selectedGoal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_goal);

        // Match the XML IDs (which are LinearLayouts)
        btnLoseWeight = findViewById(R.id.btnLoseWeight);
        btnGainMuscle = findViewById(R.id.btnGainMuscle);
        btnMaintainWeight = findViewById(R.id.btnMaintainWeight);

        backButton = findViewById(R.id.backButton);
        continueButton = findViewById(R.id.continueButton);

        // Selection logic
        btnLoseWeight.setOnClickListener(v -> {
            resetSelection();
            btnLoseWeight.setBackgroundResource(R.drawable.option_frame_blue);
            selectedGoal = "Lose Weight";
            Toast.makeText(this, "Selected: " + selectedGoal, Toast.LENGTH_SHORT).show();
        });

        btnGainMuscle.setOnClickListener(v -> {
            resetSelection();
            btnGainMuscle.setBackgroundResource(R.drawable.option_frame_blue);
            selectedGoal = "Gain Weight and Muscle";
            Toast.makeText(this, "Selected: " + selectedGoal, Toast.LENGTH_SHORT).show();
        });

        btnMaintainWeight.setOnClickListener(v -> {
            resetSelection();
            btnMaintainWeight.setBackgroundResource(R.drawable.option_frame_blue);
            selectedGoal = "Maintain Weight";
            Toast.makeText(this, "Selected: " + selectedGoal, Toast.LENGTH_SHORT).show();
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PrimaryGoal.this, Sex.class);
            startActivity(intent);
            finish();
        });

        continueButton.setOnClickListener(v -> {
            if (selectedGoal.isEmpty()) {
                Toast.makeText(this, "Please select a goal first.", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences prefs = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE);
                prefs.edit().putString("USER_GOAL", selectedGoal).apply();

                Intent intent = new Intent(PrimaryGoal.this, HeightActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void resetSelection() {
        btnLoseWeight.setBackgroundResource(R.drawable.option_frame_white);
        btnGainMuscle.setBackgroundResource(R.drawable.option_frame_white);
        btnMaintainWeight.setBackgroundResource(R.drawable.option_frame_white);
    }
}
