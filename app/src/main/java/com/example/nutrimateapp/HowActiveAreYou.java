package com.example.nutrimateapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HowActiveAreYou extends AppCompatActivity {

    LinearLayout optionSedentary, optionLight, optionModerate, optionVery, optionSuper;
    Button backButton, continueButton;
    String selectedOption = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_active_are_you); // Make sure your XML is named correctly

        optionSedentary = findViewById(R.id.option_sedentary);
        optionLight = findViewById(R.id.option_light);
        optionModerate = findViewById(R.id.option_moderate);
        optionVery = findViewById(R.id.option_very);
        optionSuper = findViewById(R.id.option_super);
        backButton = findViewById(R.id.back_button);
        continueButton = findViewById(R.id.continue_button);

        View.OnClickListener selectListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetSelections();
                int id = view.getId();
                if (id == R.id.option_sedentary) {
                    selectedOption = "Sedentary";
                } else if (id == R.id.option_light) {
                    selectedOption = "Lightly Active";
                } else if (id == R.id.option_moderate) {
                    selectedOption = "Moderately Active";
                } else if (id == R.id.option_very) {
                    selectedOption = "Very Active";
                } else if (id == R.id.option_super) {
                    selectedOption = "Super Active";
                }
            }
        };

        optionSedentary.setOnClickListener(selectListener);
        optionLight.setOnClickListener(selectListener);
        optionModerate.setOnClickListener(selectListener);
        optionVery.setOnClickListener(selectListener);
        optionSuper.setOnClickListener(selectListener);

        backButton.setOnClickListener(v -> finish());

        continueButton.setOnClickListener(v -> {
            if (selectedOption.isEmpty()) {
                Toast.makeText(this, "Please select an activity level", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Selected: " + selectedOption, Toast.LENGTH_SHORT).show();
                // TODO: Save or pass selectedOption
            }
        });
    }

    private void resetSelections() {
        optionSedentary.setBackgroundResource(R.drawable.option_frame_white);
        optionLight.setBackgroundResource(R.drawable.option_frame_white);
        optionModerate.setBackgroundResource(R.drawable.option_frame_white);
        optionVery.setBackgroundResource(R.drawable.option_frame_white);
        optionSuper.setBackgroundResource(R.drawable.option_frame_white);
    }
}
