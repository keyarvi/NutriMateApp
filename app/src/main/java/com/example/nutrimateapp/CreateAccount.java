package com.example.nutrimateapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // your login layout

        // 🔁 Forgot Password link
        TextView forgotPasswordText = findViewById(R.id.forgot); // Make sure the ID matches your XML
        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccount.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        // ✅ Login button → go to LGS
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccount.this, LGS.class);
                startActivity(intent);
                finish(); // optional
            }
        });

        // ✳️ Create Account text → go to SignUp
        TextView createAccountText = findViewById(R.id.createAccountText);
        createAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccount.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
}
