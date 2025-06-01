package com.example.saving_test.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.saving_test.database.entity.Spiel;

import java.util.List;

@Dao
public interface SpielDao {
    @Query("SELECT * FROM Spiel")
    List<Spiel> getAll();

    @Query("SELECT * FROM Spiel WHERE  bahnNr = :bahnNr AND spielerID = :spielerID AND turnierID = :turnierID LIMIT 1")
    Spiel getByPrimaryKey( int bahnNr, long spielerID, long turnierID);


    @Query("SELECT SUM(CASE WHEN SchlaegeNr = -1 THEN 0 ELSE SchlaegeNr END) FROM Spiel WHERE SpielerID = :spielerId AND TurnierID = :turnierId")
    Integer getSumOfSchlaege(long spielerId, long turnierId);

    @Insert
    void insertAll(Spiel... entries);
    @Insert
    void insert(Spiel entry);

    @Delete
    void delete(Spiel entry);
    @Update
    void update(Spiel spieler);

}