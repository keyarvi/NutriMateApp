package com.example.nutrimateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Name extends AppCompatActivity {

    private EditText userNameEditText;
    private Button backButton, continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_name);

        // Handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        userNameEditText = findViewById(R.id.userName);
        backButton = findViewById(R.id.backButton);
        continueButton = findViewById(R.id.continueButton);

        // 🔄 Clear the field on first click
        userNameEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameEditText.setText("");
                userNameEditText.setOnClickListener(null); // remove listener after first tap
            }
        });

        // 🔙 Back button: go to Personalize screen
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(Name.this, Personalize.class);
            startActivity(intent);
            finish(); // avoid activity stacking
        });

        // 👉 Continue button: save name and go to Birthday screen
        continueButton.setOnClickListener(view -> {
            String userName = userNameEditText.getText().toString().trim();

            if (userName.isEmpty()) {
                Toast.makeText(Name.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save name in SharedPreferences
            SharedPreferences prefs = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("USER_NAME", userName);
            editor.apply();

            // Go to next screen
            Intent intent = new Intent(Name.this, Birthday.class);
            startActivity(intent);
        });
    }
}
