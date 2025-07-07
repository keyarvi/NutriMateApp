package com.example.nutrimateapp.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.nutrimateapp.model.UserProfile;

@Database(entities = {UserProfile.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract UserProfileDao userProfileDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "nutrimate_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // okay for small/simple apps
                    .build();
        }
        return instance;
    }
}
