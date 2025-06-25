package com.example.nutrimateapp;

import android.app.Dialog;
import android.content.Intent;
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

        // Example: Retrieve user name passed from Name.java
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
                // Handle camera photo here (e.g., data.getExtras().get("data"))
                Uri imageUri = data.getData();
                // TODO: Pass imageUri to your food recognition logic
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                Uri selectedImage = data.getData();
                // TODO: Pass selectedImage to your food recognition logic
            }
        }
    }
}