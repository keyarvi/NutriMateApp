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
=======
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.returnToLoginText), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 🔙 "Return to Login Page" acts as Back to CreateAccount.java
        TextView returnToLogin = findViewById(R.id.returnToLoginText);
        returnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, CreateAccount.class);
                startActivity(intent);
                finish();
            }
        });

        // ▶️ "Reset Password" acts as Continue to LGS.java
        Button resetPasswordButton = findViewById(R.id.resetPasswordButton);
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Normally you'd validate email or send reset email here
                Intent intent = new Intent(ForgotPassword.this, LGS.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
