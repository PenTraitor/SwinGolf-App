package com.example.saving_test.database.entity;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
@Entity(primaryKeys = {"BahnNr","SpielerID","TurnierID"},
        foreignKeys = {
            @ForeignKey(entity = Spieler.class, parentColumns = "SpielerID", childColumns = "SpielerID"),
            @ForeignKey(entity = Turnier.class, parentColumns = "TurnierID", childColumns = "TurnierID"),
        })
public class Spiel {

    public long BahnNr;
    public long SpielerID;

    public long TurnierID;
    public int SchlaegeNr;

    public Spiel(long BahnNr, long SpielerID, long TurnierID,int SchlaegeNr) {
        this.BahnNr = BahnNr;
        this.SpielerID = SpielerID;
        this.SchlaegeNr = SchlaegeNr;
        this.TurnierID = TurnierID;
    }
}
