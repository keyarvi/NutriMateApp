package com.example.nutrimateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LifestylePace extends AppCompatActivity {

    LinearLayout optionSlow, optionSteady, optionFast;
    Button backButton, continueButton;
    String selectedPace = null;
    int calorieOffset = 0; // +150, +300, or +500

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifestyle_pace);

        optionSlow = findViewById(R.id.option_slow);
        optionSteady = findViewById(R.id.option_steady);
        optionFast = findViewById(R.id.option_fast);
        backButton = findViewById(R.id.back_button);
        continueButton = findViewById(R.id.continue_button);

        optionSlow.setOnClickListener(v -> selectPace("Slow", optionSlow, 150));
        optionSteady.setOnClickListener(v -> selectPace("Steady", optionSteady, 300));
        optionFast.setOnClickListener(v -> selectPace("Fast", optionFast, 500));

        backButton.setOnClickListener(v -> finish());

        continueButton.setOnClickListener(v -> {
            if (selectedPace != null) {
                // Save to SharedPreferences
                SharedPreferences prefs = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("USER_PACE", selectedPace);
                editor.putInt("USER_PACE_CALORIE_OFFSET", calorieOffset);
                editor.apply();

                // Navigate to next step
                Intent intent = new Intent(LifestylePace.this, HowActiveAreYou.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Please select a pace", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectPace(String pace, LinearLayout selectedLayout, int offset) {
        selectedPace = pace;
        calorieOffset = offset;

        Toast.makeText(this, "Selected: " + pace + " (+" + offset + " cal)", Toast.LENGTH_SHORT).show();

        // Reset styles
        optionSlow.setBackgroundResource(R.drawable.option_frame_white);
        optionSteady.setBackgroundResource(R.drawable.option_frame_white);
        optionFast.setBackgroundResource(R.drawable.option_frame_white);

        // Highlight selected
        selectedLayout.setBackgroundResource(R.drawable.option_frame_blue);
    }
}
