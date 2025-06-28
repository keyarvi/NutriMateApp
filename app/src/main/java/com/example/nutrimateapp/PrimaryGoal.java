package com.example.nutrimateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

        btnLoseWeight = findViewById(R.id.btnLoseWeight);
        btnGainMuscle = findViewById(R.id.btnGainMuscle);
        btnMaintainWeight = findViewById(R.id.btnMaintainWeight);
        backButton = findViewById(R.id.backButton);
        continueButton = findViewById(R.id.continueButton);

        // Selection logic
        btnLoseWeight.setOnClickListener(v -> {
            resetSelection();
            highlightSelection(btnLoseWeight);
            selectedGoal = "Lose Weight";
        });

        btnGainMuscle.setOnClickListener(v -> {
            resetSelection();
            highlightSelection(btnGainMuscle);
            selectedGoal = "Gain Weight and Muscle";
        });

        btnMaintainWeight.setOnClickListener(v -> {
            resetSelection();
            highlightSelection(btnMaintainWeight);
            selectedGoal = "Maintain Weight";
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
        resetColor(btnLoseWeight);
        resetColor(btnGainMuscle);
        resetColor(btnMaintainWeight);
        btnLoseWeight.setBackgroundResource(R.drawable.option_bg);
        btnGainMuscle.setBackgroundResource(R.drawable.option_bg);
        btnMaintainWeight.setBackgroundResource(R.drawable.option_bg);
    }

    private void highlightSelection(LinearLayout layout) {
        layout.setBackgroundResource(R.drawable.option_frame_blue);
        ImageView icon = (ImageView) layout.getChildAt(0);
        TextView label = (TextView) layout.getChildAt(1);
        icon.setColorFilter(Color.WHITE);
        label.setTextColor(Color.WHITE);
    }

    private void resetColor(LinearLayout layout) {
        ImageView icon = (ImageView) layout.getChildAt(0);
        TextView label = (TextView) layout.getChildAt(1);
        icon.setColorFilter(Color.BLACK);
        label.setTextColor(Color.BLACK);
    }
}
