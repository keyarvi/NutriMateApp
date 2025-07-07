package com.example.nutrimateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nutrimateapp.database.AppDatabase;
import com.example.nutrimateapp.model.UserProfile;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Complete extends AppCompatActivity {

    ImageView iconWaterDrop, iconSearch, iconLightning, iconMedical, mainCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        // === UI Setup ===
        iconWaterDrop = findViewById(R.id.icon_water_drop);
        iconSearch = findViewById(R.id.icon_search);
        iconLightning = findViewById(R.id.icon_lightning);
        iconMedical = findViewById(R.id.icon_medical);
        mainCheck = findViewById(R.id.main_check_circle);

        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);

        mainCheck.startAnimation(pulse);
        iconWaterDrop.startAnimation(bounce);
        iconSearch.startAnimation(bounce);
        iconLightning.startAnimation(bounce);
        iconMedical.startAnimation(bounce);

        // === SharedPreferences Retrieval ===
        SharedPreferences prefs = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE);
        String userName = prefs.getString("USER_NAME", "User");
        String birthDate = prefs.getString("USER_BIRTHDAY", "");
        String userSex = prefs.getString("USER_SEX", "Not set");
        String userGoal = prefs.getString("USER_GOAL", "Not set");
        String userPace = prefs.getString("USER_PACE", "None");
        int paceOffset = prefs.getInt("USER_PACE_CALORIE_OFFSET", 0);
        int userHeight = prefs.getInt("USER_HEIGHT", 0);
        int currentWeight = prefs.getInt("USER_CURRENT_WEIGHT", 0);
        int targetWeight = prefs.getInt("USER_TARGET_WEIGHT", 0);
        String activityLevel = prefs.getString("USER_ACTIVITY_LABEL", "Sedentary");
        float activityMultiplier = prefs.getFloat("USER_ACTIVITY_MULTIPLIER", 1.2f);

        // === Compute Age ===
        int age = 0;
        if (!birthDate.isEmpty()) {
            try {
                String[] parts = birthDate.split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]) - 1;
                int day = Integer.parseInt(parts[2]);

                Calendar today = Calendar.getInstance();
                Calendar birth = new GregorianCalendar(year, month, day);

                age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
                    age--;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // === Input Validation ===
        if (age <= 0 || currentWeight <= 0 || userHeight <= 0) {
            Log.e("NutriMate", "Invalid profile data for calculation.");
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Invalid input data. Please update your profile.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        // === BMR Calculation (Mifflin-St Jeor Equation) ===
        double bmr;
        if (userSex.equalsIgnoreCase("Male")) {
            bmr = 10 * currentWeight + 6.25 * userHeight - 5 * age + 5;
        } else {
            bmr = 10 * currentWeight + 6.25 * userHeight - 5 * age - 161;
        }

        // === TDEE Calculation ===
        double tdee = bmr * activityMultiplier;

        // === Save raw TDEE for LifestylePace Preview ===
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("USER_TDEE", (int) tdee);  // save TDEE for next screen

        // === Adjust for Goal ===
        double goalAdjustedCalories = tdee;
        switch (userGoal) {
            case "Lose Weight":
                goalAdjustedCalories -= 500;
                break;
            case "Gain Weight and Muscle":
                goalAdjustedCalories += paceOffset;
                break;
            case "Maintain":
                // no change
                break;
        }

        // === Macronutrient Calculation ===
        double proteinPerKg = 0.8;
        switch (activityLevel) {
            case "Lightly Active": proteinPerKg = 1.2; break;
            case "Moderately Active": proteinPerKg = 1.5; break;
            case "Very Active": proteinPerKg = 2.0; break;
            case "Super Active": proteinPerKg = 2.5; break;
        }

        double proteinGrams = proteinPerKg * currentWeight;
        double proteinCals = proteinGrams * 4;

        double fatCals = goalAdjustedCalories * 0.25;
        double fatGrams = fatCals / 9;

        double remainingCals = goalAdjustedCalories - (proteinCals + fatCals);
        double carbGrams = remainingCals / 4;

        // === Save targets for progress bars ===
        editor.putFloat("TARGET_CALORIES", (float) goalAdjustedCalories);
        editor.putFloat("TARGET_PROTEIN", (float) proteinGrams);
        editor.putFloat("TARGET_CARBS", (float) carbGrams);
        editor.putFloat("TARGET_FATS", (float) fatGrams);
        editor.apply();

        // === Save User Data to Room Database ===
        UserProfile user = new UserProfile();
        user.name = userName;
        user.birthday = birthDate;
        user.sex = userSex;
        user.height = userHeight;
        user.weight = currentWeight;
        user.primaryGoal = userGoal;
        user.activityLevel = activityLevel;
        user.lifestylePace = userPace;

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        db.userProfileDao().insert(user);  // save user to database

        // === Proceed to LogMeals ===
        Button continueButton = findViewById(R.id.continuebtn);
        continueButton.setOnClickListener(v -> {
            Intent intent = new Intent(Complete.this, LogMeals.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }
}
