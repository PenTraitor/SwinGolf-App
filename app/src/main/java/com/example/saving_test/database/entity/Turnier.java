package com.example.saving_test.database.entity;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = Anlage.class, parentColumns = "AnlageID", childColumns = "AnlageID")})
public class Turnier {
    @PrimaryKey(autoGenerate = true)
    public long TurnierID;
    public String Name;

    public long AnlageID;
    public Turnier(String Name , long AnlageID) {
        this.Name = Name;
        this.AnlageID =AnlageID;
    }
    @Override
    public String toString() {
        return Name;  // This will make the spinner show player names
    }
}
