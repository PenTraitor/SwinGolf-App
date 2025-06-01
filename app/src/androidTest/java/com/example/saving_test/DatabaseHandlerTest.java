package com.example.saving_test;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.saving_test.database.DatabaseHandler;
import com.example.saving_test.database.entity.Anlage;
import com.example.saving_test.database.entity.Spieler;
import com.example.saving_test.database.entity.Turnier;
import com.example.saving_test.database.GameState;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DatabaseHandlerTest {

    private DatabaseHandler dbHandler;

    @Before
    public void setUp() {
        dbHandler = new DatabaseHandler();
    }

    @After
    public void tearDown() {
        dbHandler.closeDb();
    }

    @Test
    public void testGetCurrentGameState() {
        // Add Anlage (3 holes)
        Anlage anlage = dbHandler.addAnlage("Test Course", 3);

        // Add Turnier
        Turnier turnier = dbHandler.addTurnier("Test Turnier", anlage.AnlageID);

        // Add Spieler
        Spieler alice = dbHandler.addSpieler("Alice");
        Spieler bob = dbHandler.addSpieler("Bobby");
        Spieler john = dbHandler.addSpieler("john");

        // Add Spieler to Turnier
        dbHandler.addPlayerToTurnier(turnier.TurnierID, alice.SpielerID);
        dbHandler.addPlayerToTurnier(turnier.TurnierID, bob.SpielerID);
        dbHandler.addPlayerToTurnier(turnier.TurnierID, john.SpielerID);

        dbHandler.updatePlay(alice.SpielerID,turnier.TurnierID,1,6);
        dbHandler.updatePlay(alice.SpielerID,turnier.TurnierID,2,9);
        dbHandler.updatePlay(alice.SpielerID,turnier.TurnierID,3,0);

        dbHandler.updatePlay(bob.SpielerID,turnier.TurnierID,1,4);
        dbHandler.updatePlay(bob.SpielerID,turnier.TurnierID,2,2);
        dbHandler.updatePlay(bob.SpielerID,turnier.TurnierID,3,1);

        // Get current game state
        GameState state= dbHandler.getCurrentGameState(turnier.TurnierID);
        String[][]grid =state.grid;

        // ✅ Print to debug log
        Log.d("GameState", "===== Current Game State =====");
        for (int i = 0; i < grid.length; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < grid[i].length; j++) {
                row.append(grid[i][j] != null ? grid[i][j] : "").append("\t");
            }
            Log.d("GameState", row.toString());
        }

        // Basic assertion to ensure grid isn't null or empty
        assertNotNull(grid);
        assertTrue(grid.length > 0);
    }
}
