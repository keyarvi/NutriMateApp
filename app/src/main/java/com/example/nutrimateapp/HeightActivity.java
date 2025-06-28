package com.example.nutrimateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HeightActivity extends AppCompatActivity {

    private int minHeight = 120;
    private int maxHeight = 250;
    private int selectedHeight = 180;

    private TextView heightText;
    private LinearLayout heightContainer;
    private ScrollView heightScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);

        heightText = findViewById(R.id.heightValue);
        heightContainer = findViewById(R.id.heightContainer);
        heightScroll = findViewById(R.id.heightScroll);
        Button backButton = findViewById(R.id.backButton);
        Button continueButton = findViewById(R.id.continueButton);

        populateHeightTicks();
        heightScroll.post(() -> scrollToHeight(selectedHeight));

        heightScroll.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int centerY = heightScroll.getScrollY() + heightScroll.getHeight() / 2;

            // Find the tick that is closest to the centerY
            int closestIndex = 0;
            int smallestDistance = Integer.MAX_VALUE;

            for (int i = 0; i < heightContainer.getChildCount(); i++) {
                View tickItem = heightContainer.getChildAt(i);
                int itemCenter = tickItem.getTop() + tickItem.getHeight() / 2;
                int distance = Math.abs(itemCenter - centerY);
                if (distance < smallestDistance) {
                    smallestDistance = distance;
                    closestIndex = i;
                }
            }

            if (closestIndex >= 0 && closestIndex < heightContainer.getChildCount()) {
                updateSelection(minHeight + closestIndex);
            }
        });

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(HeightActivity.this, PrimaryGoal.class));
            finish();
        });

        continueButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE);
            prefs.edit().putInt("USER_HEIGHT", selectedHeight).apply();

            startActivity(new Intent(HeightActivity.this, Weight.class));
            finish();
        });
    }

    private void populateHeightTicks() {
        for (int i = minHeight; i <= maxHeight; i++) {
            LinearLayout tickItem = new LinearLayout(this);
            tickItem.setOrientation(LinearLayout.VERTICAL);
            tickItem.setGravity(Gravity.CENTER);
            tickItem.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            // Tick line
            View tick = new View(this);
            int tickWidth = (i % 10 == 0) ? 60 : 30;
            int tickHeight = (i == selectedHeight) ? 4 : 2;
            LinearLayout.LayoutParams tickParams = new LinearLayout.LayoutParams(tickWidth, tickHeight);
            tickParams.setMargins(0, 16, 0, 0);
            tick.setLayoutParams(tickParams);
            tick.setBackgroundColor(getResources().getColor(R.color.white));
            tickItem.addView(tick);

            // Label every 10 cm
            if (i % 10 == 0) {
                TextView label = new TextView(this);
                label.setText(String.valueOf(i));
                label.setTextColor(getResources().getColor(R.color.white));
                label.setTextSize(12);
                label.setPadding(0, 8, 0, 16);
                label.setGravity(Gravity.CENTER);
                tickItem.addView(label);
            } else {
                View spacer = new View(this);
                spacer.setLayoutParams(new LinearLayout.LayoutParams(1, 32));
                tickItem.addView(spacer);
            }

            heightContainer.addView(tickItem);
        }
    }

    private void updateSelection(int newHeight) {
        selectedHeight = newHeight;
        heightText.setText(String.valueOf(selectedHeight));

        int index = newHeight - minHeight;
        int count = heightContainer.getChildCount();

        for (int i = 0; i < count; i++) {
            LinearLayout tickItem = (LinearLayout) heightContainer.getChildAt(i);
            View tick = tickItem.getChildAt(0); // first child is the tick

            int tickWidth = ((minHeight + i) % 10 == 0) ? 60 : 30;
            int tickHeight = (i == index) ? 4 : 2;

            LinearLayout.LayoutParams tickParams = (LinearLayout.LayoutParams) tick.getLayoutParams();
            tickParams.width = tickWidth;
            tickParams.height = tickHeight;
            tick.setLayoutParams(tickParams);
        }
    }

    private void scrollToHeight(int targetHeight) {
        int index = targetHeight - minHeight;
        View targetView = heightContainer.getChildAt(index);
        if (targetView != null) {
            heightScroll.smoothScrollTo(0,
                    targetView.getTop() - heightScroll.getHeight() / 2 + targetView.getHeight() / 2);
        }
    }
}
