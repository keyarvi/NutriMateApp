package com.example.nutrimateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Sex extends AppCompatActivity {

    private String selectedSex = "";
    private CardView maleCard, femaleCard;
    private TextView maleText, femaleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sex);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        maleCard = findViewById(R.id.maleCard);
        femaleCard = findViewById(R.id.femaleCard);
        maleText = findViewById(R.id.maleText);
        femaleText = findViewById(R.id.femaleText);

        // Male selected
        maleCard.setOnClickListener(view -> {
            selectedSex = "Male";
            maleCard.setBackgroundResource(R.drawable.option_frame_blue);
            femaleCard.setBackgroundResource(R.drawable.option_bg);

            maleText.setTextColor(getResources().getColor(android.R.color.white));
            femaleText.setTextColor(getResources().getColor(android.R.color.black));
        });

        // Female selected
        femaleCard.setOnClickListener(view -> {
            selectedSex = "Female";
            femaleCard.setBackgroundResource(R.drawable.option_frame_blue);
            maleCard.setBackgroundResource(R.drawable.option_bg);

            femaleText.setTextColor(getResources().getColor(android.R.color.white));
            maleText.setTextColor(getResources().getColor(android.R.color.black));
        });

        // Back button
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(Sex.this, Birthday.class);
            startActivity(intent);
            finish();
        });

        // Continue button
        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(view -> {
            if (selectedSex.isEmpty()) {
                Toast.makeText(Sex.this, "Please select your biological sex", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences prefs = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE);
                prefs.edit().putString("USER_SEX", selectedSex).apply();

                Intent intent = new Intent(Sex.this, PrimaryGoal.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
