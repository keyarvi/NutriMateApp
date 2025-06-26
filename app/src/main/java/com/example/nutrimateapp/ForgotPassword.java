package com.example.nutrimateapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPassword extends AppCompatActivity {

    EditText emailEditText;
    Button resetPasswordButton;
    TextView returnToLoginText;
    ImageView backButton, menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password); // Make sure your XML filename matches

        emailEditText = findViewById(R.id.emailEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        returnToLoginText = findViewById(R.id.returnToLoginText);
        backButton = findViewById(R.id.backButton);
        menuButton = findViewById(R.id.menuButton);

        // 🔐 Handle reset password
        resetPasswordButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else {
                // You can add actual email verification logic here
                Toast.makeText(this, "Reset link sent to " + email, Toast.LENGTH_SHORT).show();
            }
        });

        // 🔁 Return to login screen
        returnToLoginText.setOnClickListener(v -> {
            startActivity(new Intent(this, Login.class));
            finish();
        });

        // 🔙 Back icon press
        backButton.setOnClickListener(v -> onBackPressed());

        // ☰ Menu icon dummy action
        menuButton.setOnClickListener(v -> {
            Toast.makeText(this, "Menu clicked", Toast.LENGTH_SHORT).show();
        });
    }
}
