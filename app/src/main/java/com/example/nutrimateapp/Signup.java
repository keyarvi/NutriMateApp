package com.example.nutrimateapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class Signup extends AppCompatActivity {

    EditText emailField, fullNameField, usernameField, passwordField, repeatPasswordField;
    Button createButton;
    ImageView backButton, menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        emailField = findViewById(R.id.emailField);
        fullNameField = findViewById(R.id.fullNameField);
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        repeatPasswordField = findViewById(R.id.repeatPasswordField);
        createButton = findViewById(R.id.createbutton);
        backButton = findViewById(R.id.backButton);
        menuButton = findViewById(R.id.menuButton);

        createButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String name = fullNameField.getText().toString().trim();
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String repeatPassword = repeatPasswordField.getText().toString().trim();

            if (email.isEmpty() || name.isEmpty() || username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(repeatPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Signup.this, Login.class));
                finish();
            }
        });

        backButton.setOnClickListener(v -> onBackPressed());
        menuButton.setOnClickListener(v -> Toast.makeText(this, "Menu clicked", Toast.LENGTH_SHORT).show());
    }
}
