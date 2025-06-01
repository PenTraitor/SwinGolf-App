package com.example.saving_test.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.saving_test.database.entity.Spieler;

import java.util.List;


@Dao
public interface SpielerDao {
    @Query("SELECT * FROM Spieler")
    List<Spieler> getAll();

    @Query("SELECT * FROM Spieler WHERE SpielerID = (:id) Limit 1")
    Spieler getByID(long id);

    @Query("SELECT * FROM Spieler WHERE SpielerID IN (:ids)")
    List<Spieler> loadAllByIds(long[] ids);


    @Insert
    void insertAll(Spieler... entries);
    @Insert
    long insert(Spieler entry);

    @Delete
    void delete(Spieler entry);
    @Update
    void update(Spieler spieler);
}

