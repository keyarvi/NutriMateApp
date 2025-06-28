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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.reflect.Field;

public class Birthday extends AppCompatActivity {

    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_birthday);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.birthday), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        datePicker = findViewById(R.id.datePicker);
        setDatePickerWhite(datePicker); // ✅ Apply white color to text and dividers

        // 🔙 Back button to go to Name activity
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(Birthday.this, Name.class);
            startActivity(intent);
            finish();
        });

        // 👉 Continue button to go to Sex activity
        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(view -> {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1; // Month is 0-based
            int year = datePicker.getYear();

            String birthDate = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);

            // Save to SharedPreferences
            SharedPreferences prefs = getSharedPreferences("NutriMatePrefs", MODE_PRIVATE);
            prefs.edit().putString("USER_BIRTHDAY", birthDate).apply();

            Intent intent = new Intent(Birthday.this, Sex.class);
            startActivity(intent);
        });
    }

    // ✅ Method to make spinner text and lines white
    private void setDatePickerWhite(DatePicker datePicker) {
        try {
            LinearLayout layout1 = (LinearLayout) datePicker.getChildAt(0);
            LinearLayout layout2 = (LinearLayout) layout1.getChildAt(0);

            for (int i = 0; i < layout2.getChildCount(); i++) {
                NumberPicker picker = (NumberPicker) layout2.getChildAt(i);

                // Change spinner text color
                for (int j = 0; j < picker.getChildCount(); j++) {
                    View child = picker.getChildAt(j);
                    if (child instanceof EditText) {
                        ((EditText) child).setTextColor(Color.WHITE);
                    }
                }

                // Change divider line color via reflection
                Field[] fields = NumberPicker.class.getDeclaredFields();
                for (Field field : fields) {
                    if (field.getName().equals("mSelectionDivider")) {
                        field.setAccessible(true);
                        field.set(picker, new ColorDrawable(Color.WHITE));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
