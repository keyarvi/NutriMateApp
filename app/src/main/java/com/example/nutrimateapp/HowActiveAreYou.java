package com.example.nutrimateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HowActiveAreYou extends AppCompatActivity {

    LinearLayout optionSedentary, optionLight, optionModerate, optionVery, optionSuper;
    Button backButton, continueButton;

    String selectedLabel = "";
    float selectedMultiplier = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_active_are_you);

        optionSedentary = findViewById(R.id.option_sedentary);
        optionLight = findViewById(R.id.option_light);
        optionModerate = findViewById(R.id.option_moderate);
        optionVery = findViewById(R.id.option_very);
        optionSuper = findViewById(R.id.option_super);
        backButton = findViewById(R.id.back_button);
        continueButton = findViewById(R.id.continue_button);

        View.OnClickListener selectListener = view -> {
            resetSelections();

            if (view == optionSedentary) {
                selectedLabel = "Sedentary";
                selectedMultiplier = 1.2f;
            } else if (view == optionLight) {
                selectedLabel = "Lightly Active";
                selectedMultiplier = 1.375f;
            } else if (view == optionModerate) {
                selectedLabel = "Moderately Active";
                selectedMultiplier = 1.55f;
            } else if (view == optionVery) {
                selectedLabel = "Very Active";
                selectedMultiplier = 1.725f;
            } else if (view == optionSuper) {
                selectedLabel = "Super Active";
                selectedMultiplier = 1.9f;
            }

            view.setBackgroundResource(R.drawable.option_frame_blue);
            Toast.makeText(this, "Selected: " + selectedLabel, Toast.LENGTH_SHORT).show();
        };

        optionSedentary.setOnClickListener(selectListener);
        optionLight.setOnClickListener(selectListener);
        optionModerate.setOnClickListener(selectListener);
        optionVery.setOnClickListener(selectListener);
        optionSuper.setOnClickListener(selectListener);

        backButton.setOnClickListener(v -> {finish();
        });

        continueButton.setOnClickListener(v -> {
            if (selectedLabel.isEmpty()) {
                Toast.makeText(this, "Please select an activity level", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences prefs = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("USER_ACTIVITY_LABEL", selectedLabel);
                editor.putFloat("USER_ACTIVITY_MULTIPLIER", selectedMultiplier);
                editor.apply();

                Intent intent = new Intent(HowActiveAreYou.this, Complete.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void resetSelections() {
        optionSedentary.setBackgroundResource(R.drawable.option_background);
        optionLight.setBackgroundResource(R.drawable.option_background);
        optionModerate.setBackgroundResource(R.drawable.option_background);
        optionVery.setBackgroundResource(R.drawable.option_background);
        optionSuper.setBackgroundResource(R.drawable.option_background);
    }
}
