package com.example.nutrimateapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginButton;
    CheckBox rememberMeCheckBox;
    TextView forgotPasswordText, createAccountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        rememberMeCheckBox = findViewById(R.id.checkBox);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        createAccountText = findViewById(R.id.createAccountText);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (email.equals("user@example.com") && password.equals("password123")) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        forgotPasswordText.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPassword.class)));

        createAccountText.setOnClickListener(v ->
                startActivity(new Intent(this, Signup.class)));
    }
}
