package com.example.nutrimateapp;

import android.content.Intent;
import android.os.Bundle;
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

    // ✅ Sample credentials for testing
    private final String SAMPLE_EMAIL = "test@nutrimate.com";
    private final String SAMPLE_PASSWORD = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Make sure your layout file name matches

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        createAccountText = findViewById(R.id.createAccountText);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        rememberMeCheckBox = findViewById(R.id.checkBox);

        // 🔐 Login button click
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            } else if (email.equals(SAMPLE_EMAIL) && password.equals(SAMPLE_PASSWORD)) {
                boolean remember = rememberMeCheckBox.isChecked();
                if (remember) {
                    // (Optional) Save login state using SharedPreferences here
                }
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);  // continue animation
                finish();
            } else {
                Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
            }
        });

        // ➕ Go to Sign Up
        createAccountText.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Signup.class));
        });

        // ❓ Go to Forgot Password
        forgotPasswordText.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, ForgotPassword.class));
        });
    }
}
