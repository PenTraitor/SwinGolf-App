package com.example.saving_test.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;



import com.example.saving_test.database.entity.Turnier;
import com.example.saving_test.database.entity.TurnierSpielernCrossRef;

import java.util.List;

@Dao
public interface TurnierDao {
    @Query("SELECT * FROM Turnier")
    List<Turnier> getAll();

    @Query("SELECT * FROM Turnier WHERE TurnierID = (:id) Limit 1")
    Turnier getByID(long id);

    @Query("SELECT * FROM Turnier WHERE TurnierID IN (:ids)")
    List<Turnier> loadAllByIds(int[] ids);

    @Transaction
    @Query("SELECT * FROM Turnier WHERE turnierID = :idTurnier")
    TurnierMitSpieler getTurnierMitSpielern(long idTurnier);

    @Insert
    void insertAll(Turnier... entries);
    @Insert
    long insert(Turnier entry);

    @Insert
    void addPlayer(TurnierSpielernCrossRef ref);

    @Delete
    void delete(Turnier entry);
    @Update
    void update(Turnier spieler);
}