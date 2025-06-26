package com.example.nutrimateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Birthday extends AppCompatActivity {

    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_birthday);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.birthday), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        datePicker = findViewById(R.id.datePicker);

        // Back button to go to Name activity
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(Birthday.this, Name.class);
            startActivity(intent);
            finish(); // optional to prevent stacking
        });

        // Continue button to go to Sex activity
        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(view -> {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1; // Month is 0-indexed
            int year = datePicker.getYear();

            String birthDate = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);

            // Save to SharedPreferences
            SharedPreferences prefs = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE);
            prefs.edit().putString("USER_BIRTHDAY", birthDate).apply();

            Intent intent = new Intent(Birthday.this, Sex.class);
            startActivity(intent);
        });
    }
}
