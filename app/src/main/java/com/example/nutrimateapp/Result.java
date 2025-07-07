package com.example.nutrimateapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nutrimateapp.database.AppDatabase;
import com.example.nutrimateapp.model.UserProfile;

public class Result extends AppCompatActivity {

    TextView resultName, resultGoal, resultCalories, resultProtein, resultCarbs, resultFats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);

        // Handle edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // === Initialize Views ===
        resultName = findViewById(R.id.result_name);           // e.g., "Hello, John"
        resultGoal = findViewById(R.id.result_goal);           // e.g., "Your goal: Lose Weight"
        resultCalories = findViewById(R.id.result_calories);   // e.g., "Target Calories: 1800 kcal"
        resultProtein = findViewById(R.id.result_protein);     // e.g., "Protein: 120g"
        resultCarbs = findViewById(R.id.result_carbs);         // e.g., "Carbs: 200g"
        resultFats = findViewById(R.id.result_fats);           // e.g., "Fats: 60g"

        // === Load Profile from Room DB ===
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        UserProfile user = db.userProfileDao().getUser();

        if (user != null) {
            resultName.setText("Hello, " + user.name);
            resultGoal.setText("Your goal: " + user.primaryGoal);

            // Load target macros from SharedPreferences
            float targetCalories = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE)
                    .getFloat("TARGET_CALORIES", 0f);
            float protein = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE)
                    .getFloat("TARGET_PROTEIN", 0f);
            float carbs = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE)
                    .getFloat("TARGET_CARBS", 0f);
            float fats = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE)
                    .getFloat("TARGET_FATS", 0f);

            resultCalories.setText("Target Calories: " + Math.round(targetCalories) + " kcal");
            resultProtein.setText("Protein: " + Math.round(protein) + "g");
            resultCarbs.setText("Carbs: " + Math.round(carbs) + "g");
            resultFats.setText("Fats: " + Math.round(fats) + "g");
        }
    }
}
