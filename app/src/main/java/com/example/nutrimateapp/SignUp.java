package com.example.nutrimateapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup); // layout file should match

        // 🔘 Create Account button → go to LGS
        Button createAccountButton = findViewById(R.id.createbutton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, LGS.class);
                startActivity(intent);
                finish(); // Optional: prevent going back to sign up screen
            }
        });
    }
}
