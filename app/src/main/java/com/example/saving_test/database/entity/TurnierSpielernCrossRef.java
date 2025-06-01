package com.example.saving_test.database.entity;

import androidx.room.Entity;

@Entity(primaryKeys = {"SpielerID", "TurnierID"})
public class TurnierSpielernCrossRef {
    public long SpielerID;
    public long TurnierID;

    public TurnierSpielernCrossRef(long SpielerID, long TurnierID) {
        this.SpielerID = SpielerID;
        this.TurnierID = TurnierID;
    }
}
