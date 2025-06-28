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

        populateHeightList();
        heightScroll.post(() -> scrollToHeight(selectedHeight));

        heightScroll.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollY = heightScroll.getScrollY();
            int itemHeight = heightContainer.getChildAt(0).getHeight();
            int centerIndex = (scrollY + heightScroll.getHeight() / 2) / itemHeight;

            if (centerIndex >= 0 && centerIndex < heightContainer.getChildCount()) {
                updateSelection(centerIndex + minHeight);
            }
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(HeightActivity.this, Weight.class);
            startActivity(intent);
            finish();
        });

        continueButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE);
            prefs.edit().putInt("USER_HEIGHT", selectedHeight).apply();

            Intent intent = new Intent(HeightActivity.this, Weight.class);
            startActivity(intent);
            finish();
        });
    }

    private void populateHeightList() {
        for (int i = minHeight; i <= maxHeight; i++) {
            TextView tv = new TextView(this);
            tv.setText(String.valueOf(i));
            tv.setTextSize(i == selectedHeight ? 36 : 24);
            tv.setTextColor(getResources().getColor(R.color.white));
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(0, 32, 0, 32);
            heightContainer.addView(tv);
        }
    }

    private void updateSelection(int newHeight) {
        selectedHeight = newHeight;
        heightText.setText(String.valueOf(selectedHeight));

        int childCount = heightContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            TextView tv = (TextView) heightContainer.getChildAt(i);
            int value = minHeight + i;
            tv.setTextSize(value == selectedHeight ? 36 : 24);
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
