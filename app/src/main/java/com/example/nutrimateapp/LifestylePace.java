package com.example.nutrimateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LifestylePace extends AppCompatActivity {

    LinearLayout optionSlow, optionSteady, optionFast;
    Button backButton, continueButton;
    TextView paceSlowCalorie, paceSteadyCalorie, paceFastCalorie;
    TextView paceSlowText, paceSteadyText, paceFastText;
    TextView caloriePreview;

    String selectedPace = null;
    int calorieOffset = 0;
    int baseCalories = 2500; // default fallback if TDEE not yet saved

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifestyle_pace);

        // Link views
        optionSlow = findViewById(R.id.option_slow);
        optionSteady = findViewById(R.id.option_steady);
        optionFast = findViewById(R.id.option_fast);
        backButton = findViewById(R.id.back_button);
        continueButton = findViewById(R.id.continue_button);

        paceSlowCalorie = optionSlow.findViewById(R.id.pace_slow_calorie);
        paceSteadyCalorie = optionSteady.findViewById(R.id.pace_steady_calorie);
        paceFastCalorie = optionFast.findViewById(R.id.pace_fast_calorie);

        paceSlowText = optionSlow.findViewById(R.id.pace_slow_text);
        paceSteadyText = optionSteady.findViewById(R.id.pace_steady_text);
        paceFastText = optionFast.findViewById(R.id.pace_fast_text);

        caloriePreview = findViewById(R.id.calorie_preview);

        SharedPreferences prefs = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE);
        String userGoal = prefs.getString("USER_GOAL", "Maintain Weight");
        baseCalories = prefs.getInt("USER_TDEE", 2500); // fallback if not saved yet

        // Set label values based on goal
        String slowLabel = "+150 cal";
        String steadyLabel = "+300 cal";
        String fastLabel = "+500 cal";

        if (userGoal.equals("Lose Weight")) {
            slowLabel = "-150 cal";
            steadyLabel = "-300 cal";
            fastLabel = "-500 cal";

            paceSlowText.setText("Gentle calorie cut, minimal lifestyle changes");
            paceSteadyText.setText("Balanced fat loss with daily consistency");
            paceFastText.setText("Faster results with stricter tracking");

        } else if (userGoal.equals("Gain Weight and Muscle")) {
            paceSlowText.setText("Light surplus, slow muscle growth");
            paceSteadyText.setText("Moderate bulking, ideal for strength and size");
            paceFastText.setText("Aggressive muscle gain, may include fat");

        } else {
            slowLabel = "±0 cal";
            steadyLabel = "±0 cal";
            fastLabel = "±0 cal";

            paceSlowText.setText("Sustain energy balance and lifestyle");
            paceSteadyText.setText("Sustain energy balance and lifestyle");
            paceFastText.setText("Sustain energy balance and lifestyle");
        }

        paceSlowCalorie.setText(slowLabel);
        paceSteadyCalorie.setText(steadyLabel);
        paceFastCalorie.setText(fastLabel);

        optionSlow.setOnClickListener(v -> selectPace("Slow", optionSlow, 150, userGoal));
        optionSteady.setOnClickListener(v -> selectPace("Steady", optionSteady, 300, userGoal));
        optionFast.setOnClickListener(v -> selectPace("Fast", optionFast, 500, userGoal));

        backButton.setOnClickListener(v -> finish());

        continueButton.setOnClickListener(v -> {
            if (selectedPace != null) {
                SharedPreferences.Editor editor = prefs.edit();

                int adjustedOffset = calorieOffset;
                if (userGoal.equals("Lose Weight")) {
                    adjustedOffset = -calorieOffset;
                } else if (userGoal.equals("Maintain Weight")) {
                    adjustedOffset = 0;
                }

                editor.putString("USER_PACE", selectedPace);
                editor.putInt("USER_PACE_CALORIE_OFFSET", adjustedOffset);
                editor.apply();

                Intent intent = new Intent(LifestylePace.this, HowActiveAreYou.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Please select a pace", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectPace(String pace, LinearLayout selectedLayout, int offset, String userGoal) {
        selectedPace = pace;
        calorieOffset = offset;

        // Reset highlight styles
        optionSlow.setBackgroundResource(R.drawable.option_frame_white);
        optionSteady.setBackgroundResource(R.drawable.option_frame_white);
        optionFast.setBackgroundResource(R.drawable.option_frame_white);

        // Highlight selected layout
        selectedLayout.setBackgroundResource(R.drawable.option_frame_blue);

        // Show estimated calories
        int previewCalories = baseCalories;
        if (userGoal.equals("Lose Weight")) {
            previewCalories = baseCalories - offset;
        } else if (userGoal.equals("Gain Weight and Muscle")) {
            previewCalories = baseCalories + offset;
        }

        caloriePreview.setText("Estimated daily goal: " + previewCalories + " kcal");
    }
}
