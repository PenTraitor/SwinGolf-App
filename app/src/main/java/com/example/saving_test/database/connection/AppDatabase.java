package com.example.saving_test.database.connection;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.saving_test.database.entity.*;
import com.example.saving_test.database.dao.*;

@Database(entities = {Anlage.class, Spieler.class, Turnier.class, Spiel.class ,TurnierSpielernCrossRef.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "my_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    public abstract AnlageDao anlageDao();

    public abstract SpielerDao spielerDao();

    public abstract TurnierDao turnierDao();

    public abstract SpielDao spielDao();
}