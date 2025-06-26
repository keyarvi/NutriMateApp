package com.example.nutrimateapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPassword extends AppCompatActivity {

    EditText emailEditText;
    Button resetPasswordButton;
    TextView returnToLoginText;
    ImageView backButton, menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        emailEditText = findViewById(R.id.emailEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        returnToLoginText = findViewById(R.id.returnToLoginText);
        backButton = findViewById(R.id.backButton);
        menuButton = findViewById(R.id.menuButton);

        resetPasswordButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Reset link sent to " + email, Toast.LENGTH_SHORT).show();
            }
        });

        returnToLoginText.setOnClickListener(v -> {
            startActivity(new Intent(this, Login.class));
            finish();
        });

        backButton.setOnClickListener(v -> onBackPressed());
        menuButton.setOnClickListener(v -> Toast.makeText(this, "Menu clicked", Toast.LENGTH_SHORT).show());
    }
}
