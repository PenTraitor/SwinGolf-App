package com.example.saving_test.database.dao;

import com.example.saving_test.database.entity.Spieler;

import java.util.List;

public class GameState {
    public String[][] grid;
    public List<Spieler> spieler;

    public GameState(String[][] grid, List<Spieler> spieler) {
        this.grid = grid;
        this.spieler = spieler;
    }
}
