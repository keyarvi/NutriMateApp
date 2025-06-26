package com.example.nutrimateapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PrimaryGoal extends AppCompatActivity {

    Button loseWeightBtn, gainMuscleBtn, maintainWeightBtn;
    Button backButton, continueButton;

    String selectedGoal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_goal); // ✅ Match the layout file name

        // Initialize buttons
        backButton = findViewById(R.id.backButton);
        continueButton = findViewById(R.id.continueButton);

        // Goal selection buttons
        loseWeightBtn.setOnClickListener(view -> {
            selectedGoal = "Lose Weight";
            Toast.makeText(this, "Selected: " + selectedGoal, Toast.LENGTH_SHORT).show();
        });

        gainMuscleBtn.setOnClickListener(view -> {
            selectedGoal = "Gain Weight and Muscle";
            Toast.makeText(this, "Selected: " + selectedGoal, Toast.LENGTH_SHORT).show();
        });

        maintainWeightBtn.setOnClickListener(view -> {
            selectedGoal = "Maintain Weight";
            Toast.makeText(this, "Selected: " + selectedGoal, Toast.LENGTH_SHORT).show();
        });

        // Navigation buttons
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(PrimaryGoal.this, Weight.class);
            startActivity(intent);
            finish();
        });

        continueButton.setOnClickListener(view -> {
            if (selectedGoal.isEmpty()) {
                Toast.makeText(this, "Please select a goal first.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(PrimaryGoal.this, Weight.class);
                intent.putExtra("goal", selectedGoal); // Pass goal to next activity
                startActivity(intent);
                finish();
            }
        });
    }
}

