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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class LogMeals extends AppCompatActivity {

    private TextView tvUsername, tvCaloriesCount, tvProtein, tvCarbs, tvFats;
    private ProgressBar progressCalories, progressProtein, progressCarbs, progressFats;
    private Button btnAddMeal;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_IMAGE_FILE = 4;

    private float totalCalories = 0;
    private float totalProtein = 0;
    private float totalCarbs = 0;
    private float totalFats = 0;

    private float targetCalories;
    private float targetProtein;
    private float targetCarbs;
    private float targetFats;

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
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                Bundle extras = data.getExtras();
                Bitmap photo = (Bitmap) extras.get("data");
                displayDetectedMeal(photo, "Grilled Chicken with Rice", 550, 40, 45, 20);
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                Uri selectedImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    String path = selectedImage.toString();

                    if (path.contains("test1")) {
                        simulateDetectionAndShowDialog(bitmap, "Fried Chicken, Broccoli and Rice", new String[]{"Chicken", "Broccoli", "Rice"});
                    } else if (path.contains("test2")) {
                        simulateDetectionAndShowDialog(bitmap, "Fried Fish, Omelet and Rice", new String[]{"Fried Fish", "Omelet", "Rice"});
                    } else if (path.contains("test3")) {
                        simulateDetectionAndShowDialog(bitmap, "Omelet and Fried Rice", new String[]{"Omelet", "Fried Rice", ""});
                    } else if (path.contains("test4")) {
                        simulateDetectionAndShowDialog(bitmap, "Beef Steak and Rice", new String[]{"Beef Steak", "Rice", ""});
                    } else if (path.contains("test5")) {
                        simulateDetectionAndShowDialog(bitmap, "Beef Curry and Rice", new String[]{"Beef Curry", "Rice", ""});
                    } else if (path.contains("test6")) {
                        simulateDetectionAndShowDialog(bitmap, "Sausage and Egg", new String[]{"Sausage", "Egg Sunny-side Up", ""});
                    } else {
                        displayDetectedMeal(bitmap, "Spaghetti Bolognese", 620, 35, 70, 18);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
        }, 1500); // 1.5 second delay
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
            int g1 = parseInput(input1.getText().toString());
            int g2 = parseInput(input2.getText().toString());
            int g3 = parseInput(input3.getText().toString());

            float cal = 0, pro = 0, carb = 0, fat = 0;

            switch (mealName) {
                case "Fried Chicken, Broccoli and Rice":
                    cal = (g1 * 2.6f) + (g2 * 0.34f) + (g3 * 1.3f);
                    pro = (g1 * 0.24f) + (g2 * 0.028f) + (g3 * 0.027f);
                    carb = (g1 * 0.06f) + (g2 * 0.066f) + (g3 * 0.28f);
                    fat = (g1 * 0.15f) + (g2 * 0.004f) + (g3 * 0.003f);
                    break;
                case "Fried Fish, Omelet and Rice":
                    cal = (g1 * 2.2f) + (g2 * 1.54f) + (g3 * 1.3f);
                    pro = (g1 * 0.15f) + (g2 * 0.10f) + (g3 * 0.027f);
                    carb = (g1 * 0.10f) + (g2 * 0.015f) + (g3 * 0.28f);
                    fat = (g1 * 0.12f) + (g2 * 0.12f) + (g3 * 0.003f);
                    break;
                case "Omelet and Fried Rice":
                    cal = (g1 * 1.54f) + (g2 * 1.8f);
                    pro = (g1 * 0.10f) + (g2 * 0.06f);
                    carb = (g1 * 0.015f) + (g2 * 0.24f);
                    fat = (g1 * 0.12f) + (g2 * 0.07f);
                    break;
                case "Beef Steak and Rice":
                    cal = (g1 * 2.71f) + (g2 * 1.3f);
                    pro = (g1 * 0.25f) + (g2 * 0.027f);
                    carb = (g1 * 0.0f) + (g2 * 0.28f);
                    fat = (g1 * 0.20f) + (g2 * 0.003f);
                    break;
                case "Beef Curry and Rice":
                    cal = (g1 * 2.1f) + (g2 * 1.3f);
                    pro = (g1 * 0.09f) + (g2 * 0.027f);
                    carb = (g1 * 0.26f) + (g2 * 0.28f);
                    fat = (g1 * 0.09f) + (g2 * 0.003f);
                    break;
                case "Sausage and Egg":
                    cal = (g1 * 3.0f) + (g2 * 1.43f);
                    pro = (g1 * 0.12f) + (g2 * 0.10f);
                    carb = (g1 * 0.02f) + (g2 * 0.011f);
                    fat = (g1 * 0.27f) + (g2 * 0.11f);
                    break;
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

        ImageView imgMeal = mealCard.findViewById(R.id.imgMeal);
        TextView tvMealName = mealCard.findViewById(R.id.tvMealName);
        TextView tvMealNutrition = mealCard.findViewById(R.id.tvMealNutrition);

        imgMeal.setImageBitmap(mealImage);
        tvMealName.setText(mealName);
        tvMealNutrition.setText(protein + "g Protein  |  " + carbs + "g Carbs  |  " + fats + "g Fats");

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
