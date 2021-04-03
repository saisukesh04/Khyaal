package com.undamped.khyaal.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.undamped.khyaal.entity.Medicine;

import java.util.List;

@Dao
public interface MedDao {

    @Insert
    void insertMed(Medicine medicine);

    @Delete
    void deleteMed(Medicine medicine);

    @Query("SELECT * FROM medicineDb")
    List<Medicine> loadAllMedicines();

    @Query("DELETE FROM medicineDb")
    void clearDb();
}
