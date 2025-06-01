package com.example.saving_test.database.dao;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.saving_test.database.entity.*;

import java.util.List;

public class TurnierMitSpieler {
    @Embedded
    public Turnier turnier;
    @Relation(
            parentColumn = "TurnierID",
            entityColumn = "SpielerID",
            associateBy = @Junction(TurnierSpielernCrossRef.class)
    )
    public List<Spieler> spieler;
}
