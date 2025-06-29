package com.example.nutrimateapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    private Bitmap capturedImage;

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

    private void openImageFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_IMAGE_FILE);
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
                    if (selectedImage.toString().contains("test1")) {
                        showPortionInputDialog(bitmap);
                    } else {
                        displayDetectedMeal(bitmap, "Spaghetti Bolognese", 620, 35, 70, 18);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_IMAGE_FILE && data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        displayDetectedMeal(bitmap, "Uploaded Image Meal", 480, 30, 50, 15);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Unable to load image.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void showPortionInputDialog(Bitmap image) {
        Dialog dialog = new Dialog(LogMeals.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_portion_input, null);
        dialog.setContentView(view);

        EditText inputChicken = view.findViewById(R.id.inputChicken);
        EditText inputBroccoli = view.findViewById(R.id.inputBroccoli);
        EditText inputRice = view.findViewById(R.id.inputRice);
        Button btnSubmit = view.findViewById(R.id.btnSubmitPortions);

        btnSubmit.setOnClickListener(v -> {
            int gChicken = parseInput(inputChicken.getText().toString());
            int gBroccoli = parseInput(inputBroccoli.getText().toString());
            int gRice = parseInput(inputRice.getText().toString());

            float cal = (gChicken * 2.6f) + (gBroccoli * 0.34f) + (gRice * 1.3f);
            float pro = (gChicken * 0.24f) + (gBroccoli * 0.028f) + (gRice * 0.027f);
            float carb = (gChicken * 0.06f) + (gBroccoli * 0.066f) + (gRice * 0.28f);
            float fat = (gChicken * 0.15f) + (gBroccoli * 0.004f) + (gRice * 0.003f);

            displayDetectedMeal(
                    image,
                    "Fried Chicken, Broccoli and Rice",
                    Math.round(cal),
                    Math.round(pro),
                    Math.round(carb),
                    Math.round(fat)
            );

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
        tvMealNutrition.setText(
                        protein + "g Protein  |  " +
                        carbs + "g Carbs  |  " +
                        fats + "g Fats"
        );

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
