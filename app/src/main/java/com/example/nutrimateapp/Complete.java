package com.example.nutrimateapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Complete extends AppCompatActivity {

    LinearLayout starsLayout;
    ImageView[] stars = new ImageView[5];
    ImageView iconWaterDrop, iconSearch, iconLightning, iconMedical, mainCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        // UI Setup
        starsLayout = findViewById(R.id.stars_layout);
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

        for (int i = 0; i < 5; i++) {
            stars[i] = (ImageView) starsLayout.getChildAt(i);
            final int index = i;
            stars[i].setOnClickListener(v ->
                    new AlertDialog.Builder(this)
                            .setMessage("You rated " + (index + 1) + " star" + (index == 0 ? "" : "s"))
                            .setPositiveButton("OK", null)
                            .show()
            );
        }

        // === SharedPreferences Retrieval ===
        SharedPreferences prefs = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE);
        String userName = prefs.getString("USER_NAME", "User");
        String birthDate = prefs.getString("USER_BIRTHDAY", "");
        String userSex = prefs.getString("USER_SEX", "Not set");
        String userGoal = prefs.getString("USER_GOAL", "Not set");
        String userPace = prefs.getString("USER_PACE", "None");
        int paceOffset = prefs.getInt("USER_PACE_CALORIE_OFFSET", 0);
        int userHeight = prefs.getInt("USER_HEIGHT", 0); // cm
        int currentWeight = prefs.getInt("USER_CURRENT_WEIGHT", 0); // kg
        int targetWeight = prefs.getInt("USER_TARGET_WEIGHT", 0);
        String activityLevel = prefs.getString("USER_ACTIVITY_LEVEL", "Sedentary");

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

        // === BMR Calculation (Mifflin-St Jeor Equation) ===
        double bmr;
        if (userSex.equalsIgnoreCase("Male")) {
            bmr = 10 * currentWeight + 6.25 * userHeight - 5 * age + 5;
        } else {
            bmr = 10 * currentWeight + 6.25 * userHeight - 5 * age - 161;
        }

        // === TDEE Multiplier ===
        double activityMultiplier = 1.2;
        switch (activityLevel) {
            case "Lightly Active": activityMultiplier = 1.375; break;
            case "Moderately Active": activityMultiplier = 1.55; break;
            case "Very Active": activityMultiplier = 1.725; break;
            case "Super Active": activityMultiplier = 1.9; break;
        }

        double tdee = bmr * activityMultiplier;

        // === Adjust for Goal ===
        double goalAdjustedCalories = tdee;
        switch (userGoal) {
            case "Lose Weight": goalAdjustedCalories -= 500; break;
            case "Gain Weight and Muscle": goalAdjustedCalories += 300; break;
        }

        // === Add Lifestyle Pace Offset ===
        goalAdjustedCalories += paceOffset;

        // === Macronutrient Calculations ===
        double proteinPerKg = 0.8;
        switch (activityLevel) {
            case "Lightly Active": proteinPerKg = 1.0; break;
            case "Moderately Active": proteinPerKg = 1.4; break;
            case "Very Active": proteinPerKg = 1.8; break;
            case "Super Active": proteinPerKg = 2.2; break;
        }

        double proteinGrams = proteinPerKg * currentWeight;
        double proteinCals = proteinGrams * 4;

        double fatCals = goalAdjustedCalories * 0.25;
        double fatGrams = fatCals / 9;

        double remainingCals = goalAdjustedCalories - (proteinCals + fatCals);
        double carbGrams = remainingCals / 4;

        // === Save Macronutrients and Calories for Progress Bars ===
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("TARGET_CALORIES", (float) goalAdjustedCalories);
        editor.putFloat("TARGET_PROTEIN", (float) proteinGrams);
        editor.putFloat("TARGET_CARBS", (float) carbGrams);
        editor.putFloat("TARGET_FATS", (float) fatGrams);
        editor.apply();

        // === Display Final Summary via AlertDialog ===
        String summary = "Hi " + userName +
                "\nAge: " + age +
                "\nSex: " + userSex +
                "\nHeight: " + userHeight + " cm" +
                "\nWeight: " + currentWeight + " kg" +
                "\nActivity: " + activityLevel +
                "\nGoal: " + userGoal +
                "\nPace: " + userPace + " (+" + paceOffset + " cal)" +
                "\n\nCalories: " + (int) goalAdjustedCalories + " kcal" +
                "\nProtein: " + (int) proteinGrams + " g" +
                "\nFats: " + (int) fatGrams + " g" +
                "\nCarbs: " + (int) carbGrams + " g";

        new AlertDialog.Builder(this)
                .setTitle("Nutrition Summary")
                .setMessage(summary)
                .setPositiveButton("OK", null)
                .show();

        Log.d("NutriMate_FinalSummary", summary);
    }
}
