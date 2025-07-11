// LogMeals.java
package com.example.nutrimateapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class LogMeals extends AppCompatActivity {

    private TextView tvUsername, tvCaloriesCount, tvProtein, tvCarbs, tvFats;
    private ProgressBar progressCalories, progressProtein, progressCarbs, progressFats;
    private Button btnAddMeal;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private float totalCalories = 0, totalProtein = 0, totalCarbs = 0, totalFats = 0;
    private float targetCalories, targetProtein, targetCarbs, targetFats;
    private Map<String, float[]> foodNutritionMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_meals);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvUsername = findViewById(R.id.userName);
        btnAddMeal = findViewById(R.id.btnAddMeal);

        tvCaloriesCount = findViewById(R.id.tvCaloriesCount);
        tvProtein = findViewById(R.id.tvProtein);
        tvCarbs = findViewById(R.id.tvCarbs);
        tvFats = findViewById(R.id.tvFats);

        progressCalories = findViewById(R.id.progressCalories);
        progressProtein = findViewById(R.id.progressProtein);
        progressCarbs = findViewById(R.id.progressCarbs);
        progressFats = findViewById(R.id.progressFats);

        loadUserTargetGoals();
        loadCSV();

        btnAddMeal.setOnClickListener(v -> showAddMealDialog());
    }

    private void loadUserTargetGoals() {
        SharedPreferences prefs = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE);
        String userName = prefs.getString("USER_NAME", "User");
        targetCalories = prefs.getFloat("TARGET_CALORIES", 2000);
        targetProtein = prefs.getFloat("TARGET_PROTEIN", 100);
        targetCarbs = prefs.getFloat("TARGET_CARBS", 250);
        targetFats = prefs.getFloat("TARGET_FATS", 70);

        tvUsername.setText("Hello, " + userName + "!");
        updateProgressBars();
    }

    private void loadCSV() {
        foodNutritionMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getAssets().open("food_nutrition_sample.csv")))) {

            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String name = parts[1].toLowerCase().trim();
                    float cal = Float.parseFloat(parts[2]);
                    float pro = Float.parseFloat(parts[3]);
                    float carb = Float.parseFloat(parts[4]);
                    float fat = Float.parseFloat(parts[5]);
                    foodNutritionMap.put(name, new float[]{cal, pro, carb, fat});
                }
            }
        } catch (IOException e) {
            Toast.makeText(this, "Failed to load CSV", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddMealDialog() {
        Dialog dialog = new Dialog(LogMeals.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_meal, null);
        dialog.setContentView(view);

        LinearLayout btnPhotoEntry = view.findViewById(R.id.btnPhotoEntry);
        LinearLayout btnUploadPicture = view.findViewById(R.id.btnUploadPicture);

        btnPhotoEntry.setOnClickListener(v -> {
            dialog.dismiss();
            openCamera();
        });

        btnUploadPicture.setOnClickListener(v -> {
            dialog.dismiss();
            openGallery();
        });

        dialog.show();
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap bitmap = null;
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                Uri selectedImage = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bitmap != null) {
                List<String> detectedItems = runFakeFoodDetection(bitmap);
                simulateDetectionAndShowDialog(bitmap, "Mixed Meal", detectedItems.toArray(new String[0]));
            }
        }
    }

    private List<String> runFakeFoodDetection(Bitmap image) {
        // Simulate detection by returning food names from CSV
        return new ArrayList<>(foodNutritionMap.keySet()).subList(0, 3); // first 3 items
    }

    private void simulateDetectionAndShowDialog(Bitmap bitmap, String mealName, String[] items) {
        Dialog loadingDialog = new Dialog(LogMeals.this);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        new Handler().postDelayed(() -> {
            loadingDialog.dismiss();
            showMealPortionDialog(bitmap, mealName, items);
        }, 1500);
    }

    private void showMealPortionDialog(Bitmap image, String mealName, String[] items) {
        Dialog dialog = new Dialog(LogMeals.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_portion_input, null);
        dialog.setContentView(view);

        TextView label1 = view.findViewById(R.id.labelItem1);
        TextView label2 = view.findViewById(R.id.labelItem2);
        TextView label3 = view.findViewById(R.id.labelItem3);
        EditText input1 = view.findViewById(R.id.inputItem1);
        EditText input2 = view.findViewById(R.id.inputItem2);
        EditText input3 = view.findViewById(R.id.inputItem3);
        Button btnSubmit = view.findViewById(R.id.btnSubmitPortions);

        label1.setText(items[0]);
        label2.setText(items[1]);
        label3.setText(items[2]);

        input1.setHint(items[0] + " in grams");
        input2.setHint(items[1] + " in grams");
        input3.setHint(items[2] + " in grams");

        btnSubmit.setOnClickListener(v -> {
            float cal = 0, pro = 0, carb = 0, fat = 0;
            int[] grams = new int[]{parseInput(input1.getText().toString()), parseInput(input2.getText().toString()), parseInput(input3.getText().toString())};

            for (int i = 0; i < 3; i++) {
                String name = items[i].toLowerCase();
                if (foodNutritionMap.containsKey(name)) {
                    float[] values = foodNutritionMap.get(name);
                    cal += values[0] * grams[i] / 100f;
                    pro += values[1] * grams[i] / 100f;
                    carb += values[2] * grams[i] / 100f;
                    fat += values[3] * grams[i] / 100f;
                }
            }

            displayDetectedMeal(image, mealName, Math.round(cal), Math.round(pro), Math.round(carb), Math.round(fat));
            dialog.dismiss();
        });

        dialog.show();
    }

    private int parseInput(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private void displayDetectedMeal(Bitmap mealImage, String mealName, int calories, int protein, int carbs, int fats) {
        LinearLayout placeholder = findViewById(R.id.placeholderNoMeals);
        LinearLayout container = findViewById(R.id.mealContainer);

        placeholder.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);

        View mealCard = LayoutInflater.from(this).inflate(R.layout.item_meal_entry, container, false);
        ((ImageView) mealCard.findViewById(R.id.imgMeal)).setImageBitmap(mealImage);
        ((TextView) mealCard.findViewById(R.id.tvMealName)).setText(mealName);
        ((TextView) mealCard.findViewById(R.id.tvMealNutrition)).setText(protein + "g Protein | " + carbs + "g Carbs | " + fats + "g Fats");
        container.addView(mealCard);

        totalCalories += calories;
        totalProtein += protein;
        totalCarbs += carbs;
        totalFats += fats;

        updateProgressBars();
    }

    private void updateProgressBars() {
        int calProgress = Math.min(100, (int) ((totalCalories / targetCalories) * 100));
        int proProgress = Math.min(100, (int) ((totalProtein / targetProtein) * 100));
        int carbProgress = Math.min(100, (int) ((totalCarbs / targetCarbs) * 100));
        int fatProgress = Math.min(100, (int) ((totalFats / targetFats) * 100));

        progressCalories.setProgress(calProgress);
        progressProtein.setProgress(proProgress);
        progressCarbs.setProgress(carbProgress);
        progressFats.setProgress(fatProgress);

        tvCaloriesCount.setText(((int) totalCalories) + " / " + ((int) targetCalories) + " cal");
        tvProtein.setText("Protein\n" + ((int) totalProtein) + "/" + ((int) targetProtein) + "g");
        tvCarbs.setText("Carbs\n" + ((int) totalCarbs) + "/" + ((int) targetCarbs) + "g");
        tvFats.setText("Fats\n" + ((int) totalFats) + "/" + ((int) targetFats) + "g");
    }
}
