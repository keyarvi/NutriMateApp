package com.example.nutrimateapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.nutrimateapp.model.UserProfile;

@Dao
public interface UserProfileDao {

    @Insert
    void insert(UserProfile user);

    @Query("SELECT * FROM user_profile LIMIT 1")
    UserProfile getUser();
}
