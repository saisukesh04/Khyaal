package com.undamped.khyaal.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.undamped.khyaal.entity.Medicine;

@Database(entities = {Medicine.class}, version = 1, exportSchema = false)
public abstract class MedDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "medicineDb";
    private static MedDatabase sInstance;

    public static MedDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(),
                    MedDatabase.class, MedDatabase.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return sInstance;
    }

    public abstract MedDao medDao();
}
