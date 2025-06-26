package com.example.nutrimateapp;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Complete extends AppCompatActivity {

    LinearLayout starsLayout;
    ImageView[] stars = new ImageView[5];
    ImageView iconWaterDrop, iconSearch, iconLightning, iconMedical, mainCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete); // Your XML filename

        // Reference views
        starsLayout = findViewById(R.id.stars_layout);
        iconWaterDrop = findViewById(R.id.icon_water_drop);
        iconSearch = findViewById(R.id.icon_search);
        iconLightning = findViewById(R.id.icon_lightning);
        iconMedical = findViewById(R.id.icon_medical);
        mainCheck = findViewById(R.id.main_check_circle);

        // Load animations
        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);

        mainCheck.startAnimation(pulse);
        iconWaterDrop.startAnimation(bounce);
        iconSearch.startAnimation(bounce);
        iconLightning.startAnimation(bounce);
        iconMedical.startAnimation(bounce);

        // Get stars and make them clickable
        for (int i = 0; i < 5; i++) {
            stars[i] = (ImageView) starsLayout.getChildAt(i);
            final int index = i;
            stars[i].setOnClickListener(v -> {
                Toast.makeText(this, "You rated " + (index + 1) + " star" + (index == 0 ? "" : "s"), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
