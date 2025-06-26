package com.example.nutrimateapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class LogMeals extends AppCompatActivity {

    private TextView tvUsername;
    private Button btnAddMeal;
    private FloatingActionButton fabCamera;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

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

        tvUsername = findViewById(R.id.tvUsername);
        btnAddMeal = findViewById(R.id.btnAddMeal);
        fabCamera = findViewById(R.id.fabCamera);

        // Retrieve username from intent
        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName != null && !userName.isEmpty()) {
            tvUsername.setText("Hello, " + userName + "!");
        }

        btnAddMeal.setOnClickListener(v -> showAddMealDialog());
        fabCamera.setOnClickListener(v -> openCamera());
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

                // Simulate image recognition and display
                displayDetectedMeal(photo, "Grilled Chicken with Rice", 550, 40, 45, 20);
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                Uri selectedImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                    // Simulate image recognition and display
                    displayDetectedMeal(bitmap, "Spaghetti Bolognese", 620, 35, 70, 18);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayDetectedMeal(Bitmap mealImage, String mealName, int calories, int protein, int carbs, int fats) {
        LinearLayout placeholder = findViewById(R.id.placeholderNoMeals);
        LinearLayout container = findViewById(R.id.mealContainer);

        // Hide the "No Meals" placeholder and show container
        placeholder.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);

        // Inflate a new meal card layout
        View mealCard = LayoutInflater.from(this).inflate(R.layout.item_meal_entry, container, false);

        ImageView imgMeal = mealCard.findViewById(R.id.imgMeal);
        TextView tvMealName = mealCard.findViewById(R.id.tvMealName);
        TextView tvMealNutrition = mealCard.findViewById(R.id.tvMealNutrition);

        imgMeal.setImageBitmap(mealImage);
        tvMealName.setText(mealName);
        tvMealNutrition.setText(
                calories + " cal  |  " +
                        protein + "g Protein  |  " +
                        carbs + "g Carbs  |  " +
                        fats + "g Fats"
        );

        container.addView(mealCard);
    }
}
