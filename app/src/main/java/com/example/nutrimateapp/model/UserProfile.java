package com.example.nutrimateapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_profile")
public class UserProfile {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String birthday;
    public String sex;
    public float height;
    public float weight;
    public String primaryGoal;
    public String activityLevel;
    public String lifestylePace;
}
