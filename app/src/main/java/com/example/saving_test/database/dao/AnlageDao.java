package com.example.saving_test.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.saving_test.database.entity.Anlage;



import java.util.List;

@Dao
public interface AnlageDao {
    @Query("SELECT * FROM Anlage")
    List<Anlage> getAll();

    @Query("SELECT * FROM Anlage WHERE AnlageID = (:id) Limit 1")
    Anlage getByID(long id);
    @Query("SELECT * FROM Anlage WHERE AnlageID IN (:ids)")
    List<Anlage> loadAllByIds(int[] ids);

    @Insert
    void insertAll(Anlage... entries);
    @Insert
    long insert(Anlage entry);

    @Delete
    void delete(Anlage entry);
    @Update
    void update(Anlage spieler);
}