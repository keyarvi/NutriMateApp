package com.example.nutrimateapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {

    TextInputEditText emailEditText, passwordEditText;
    Button loginButton;
    TextView createAccountText, forgotPasswordText;
    CheckBox rememberMeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // 🛠 Make sure your file is activity_login.xml

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        createAccountText = findViewById(R.id.createAccountText);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        rememberMeCheckBox = findViewById(R.id.checkBox);

        // Login button click
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            } else {
                boolean remember = rememberMeCheckBox.isChecked();
                // Optional: store preference if rememberMe
                Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, MainActivity.class)); // ✔ Replace with your main screen
                finish();
            }
        });

        // Go to Sign Up
        createAccountText.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Signup.class);
            startActivity(intent);
        });

        // Go to Forgot Password
        forgotPasswordText.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, ForgotPassword.class);
            startActivity(intent);
        });
    }
}
