package com.example.saving_test;

import static org.junit.Assert.*;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.saving_test.database.connection.AppDatabase;
import com.example.saving_test.database.dao.*;
import com.example.saving_test.database.entity.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class AppDatabaseTest {

    private AppDatabase db;
    private AnlageDao anlageDao;
    private SpielerDao spielerDao;
    private TurnierDao turnierDao;
    private SpielDao spielDao;

    @Before
    public void createDb() {
        db = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        AppDatabase.class)
                .allowMainThreadQueries()  // only for testing
                .build();

        anlageDao = db.anlageDao();
        spielerDao = db.spielerDao();
        turnierDao = db.turnierDao();
        spielDao = db.spielDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertAndRetrieveAllEntities() {
        // Anlage
        Anlage anlage = new Anlage( "TestAlley",5);
        long anlageId = anlageDao.insert(anlage);


        List<Anlage> anlageList = anlageDao.getAll();
        assertEquals(1, anlageList.size());
        assertEquals("TestAlley", anlageList.get(0).Name);

        // Spieler
        Spieler player = new Spieler("Test Player");
        long spielerId = spielerDao.insert(player);

        List<Spieler> spielerList = spielerDao.getAll();
        assertEquals(1, spielerList.size());
        assertEquals("Test Player", spielerList.get(0).Name);

        // Turnier
        Turnier turnier = new Turnier("Test Cup",anlageId);

        long turnierId = turnierDao.insert(turnier);

        List<Turnier> turnierList = turnierDao.getAll();
        assertEquals(1, turnierList.size());
        assertEquals("Test Cup", turnierList.get(0).Name);

        // Spiel
        Spiel spiel = new Spiel(anlageId,1,spielerId,12);
        spielDao.insert(spiel);

        List<Spiel> spiele = spielDao.getAll();
        assertEquals(1, spiele.size());
        assertEquals(12, spiele.get(0).SchlaegeNr);
    }
}
