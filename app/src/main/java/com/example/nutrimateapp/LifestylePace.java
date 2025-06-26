package com.example.nutrimateapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LifestylePace extends AppCompatActivity {

    LinearLayout optionSlow, optionSteady, optionFast;
    Button backButton, continueButton;
    String selectedPace = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifestyle_pace);

        optionSlow = findViewById(R.id.option_slow);
        optionSteady = findViewById(R.id.option_steady);
        optionFast = findViewById(R.id.option_fast);
        backButton = findViewById(R.id.back_button);
        continueButton = findViewById(R.id.continue_button);

        optionSlow.setOnClickListener(v -> {
            selectedPace = "Slow";
            Toast.makeText(this, "Selected: Slow", Toast.LENGTH_SHORT).show();
        });

        optionSteady.setOnClickListener(v -> {
            selectedPace = "Steady";
            Toast.makeText(this, "Selected: Steady", Toast.LENGTH_SHORT).show();
        });

        optionFast.setOnClickListener(v -> {
            selectedPace = "Fast";
            Toast.makeText(this, "Selected: Fast", Toast.LENGTH_SHORT).show();
        });

        backButton.setOnClickListener(v -> finish());

        continueButton.setOnClickListener(v -> {
            if (selectedPace != null) {
                Toast.makeText(this, "Continuing with: " + selectedPace, Toast.LENGTH_SHORT).show();
                // TODO: Start next activity or logic here
            } else {
                Toast.makeText(this, "Please select a pace", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
