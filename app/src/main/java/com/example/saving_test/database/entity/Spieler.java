package com.example.saving_test.database.entity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Spieler {
    @PrimaryKey(autoGenerate = true)
    public long SpielerID;
    public String Name;

    public Spieler(String Name) {
        this.Name = Name;

    }
    @Override
    public String toString() {
        return Name;  // This will make the spinner show player names
    }
}
