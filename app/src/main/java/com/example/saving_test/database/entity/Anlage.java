package com.example.saving_test.database.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Anlage {
    @PrimaryKey(autoGenerate = true)
    public long AnlageID;
    public String Name;
    public int AnzahlBahnen;

    public Anlage(String Name, int AnzahlBahnen) {
        this.Name = Name;
        this.AnzahlBahnen = AnzahlBahnen;
    }
    @Override
    public String toString() {
        return Name;  // This will make the spinner show player names
    }
}
