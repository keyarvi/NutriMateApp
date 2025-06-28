package com.example.nutrimateapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.reflect.Field;

public class Birthday extends AppCompatActivity {

    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Prevent dark mode from messing with colors
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_birthday);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.birthday), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        datePicker = findViewById(R.id.datePicker);
        setDatePickerWhite(datePicker); // ✅ White text and dividers (safe)

        // Back button
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(Birthday.this, Name.class);
            startActivity(intent);
            finish();
        });

        // Continue button
        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(view -> {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1;
            int year = datePicker.getYear();

            String birthDate = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);

            SharedPreferences prefs = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE);
            prefs.edit().putString("USER_BIRTHDAY", birthDate).apply();

            Intent intent = new Intent(Birthday.this, Sex.class);
            startActivity(intent);
        });
    }

    // ✅ Safe way to make spinner text & dividers white
    private void setDatePickerWhite(DatePicker datePicker) {
        datePicker.post(() -> {
            try {
                LinearLayout layout1 = (LinearLayout) datePicker.getChildAt(0);
                LinearLayout layout2 = (LinearLayout) layout1.getChildAt(0);

                for (int i = 0; i < layout2.getChildCount(); i++) {
                    View child = layout2.getChildAt(i);
                    if (child instanceof NumberPicker) {
                        NumberPicker picker = (NumberPicker) child;

                        // Set white text
                        for (int j = 0; j < picker.getChildCount(); j++) {
                            View pickerChild = picker.getChildAt(j);
                            if (pickerChild instanceof EditText) {
                                ((EditText) pickerChild).setTextColor(Color.WHITE);
                            }
                        }

                        // Set white divider line
                        Field[] fields = NumberPicker.class.getDeclaredFields();
                        for (Field field : fields) {
                            if (field.getName().equals("mSelectionDivider")) {
                                field.setAccessible(true);
                                field.set(picker, new ColorDrawable(Color.WHITE));
                                break;
                            }
                        }

                        picker.invalidate(); // Refresh
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
