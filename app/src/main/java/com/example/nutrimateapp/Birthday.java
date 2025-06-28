package com.example.nutrimateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class Birthday extends AppCompatActivity {

    private NumberPicker dayPicker, monthPicker, yearPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_birthday);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.birthday), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dayPicker = findViewById(R.id.dayPicker);
        monthPicker = findViewById(R.id.monthPicker);
        yearPicker = findViewById(R.id.yearPicker);

        setupPickers();

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            startActivity(new Intent(Birthday.this, Name.class));
            finish();
        });

        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(view -> {
            int day = dayPicker.getValue();
            int month = monthPicker.getValue(); // 1-12
            int year = yearPicker.getValue();

            Calendar selected = Calendar.getInstance();
            selected.set(year, month - 1, day);

            Calendar today = Calendar.getInstance();
            if (selected.after(today)) {
                dayPicker.setValue(today.get(Calendar.DAY_OF_MONTH));
                monthPicker.setValue(today.get(Calendar.MONTH) + 1);
                yearPicker.setValue(today.get(Calendar.YEAR));
                return;
            }

            String birthDate = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);

            SharedPreferences prefs = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE);
            prefs.edit().putString("USER_BIRTHDAY", birthDate).apply();

            startActivity(new Intent(Birthday.this, Sex.class));
        });
    }

    private void setupPickers() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        final String[] monthNames = {
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };

        // Month Picker
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setDisplayedValues(monthNames);
        monthPicker.setValue(1);
        setTextColor(monthPicker, Color.WHITE);

        // Day Picker
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);
        dayPicker.setValue(1);
        setTextColor(dayPicker, Color.WHITE);

        // Year Picker
        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(currentYear);
        yearPicker.setValue(currentYear - 18); // Default age 18
        setTextColor(yearPicker, Color.WHITE);

        // Update day count on month/year change
        monthPicker.setOnValueChangedListener((picker, oldVal, newVal) -> updateDayRange());
        yearPicker.setOnValueChangedListener((picker, oldVal, newVal) -> updateDayRange());
    }

    private void updateDayRange() {
        int month = monthPicker.getValue();
        int year = yearPicker.getValue();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int currentDay = dayPicker.getValue();
        dayPicker.setMaxValue(maxDays);
        if (currentDay > maxDays) {
            dayPicker.setValue(maxDays);
        }
    }

    private void setTextColor(NumberPicker picker, int color) {
        int count = picker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = picker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    ((EditText) child).setTextColor(color);
                    picker.invalidate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
