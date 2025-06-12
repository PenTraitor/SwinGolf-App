package com.example.saving_test.database;

import android.content.Context;

import androidx.room.Room;

import com.example.saving_test.database.connection.AppDatabase;
import com.example.saving_test.database.dao.*;
import com.example.saving_test.database.entity.*;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

public class DatabaseHandler {
    public static final int y = 1;
    private AppDatabase db;
    private AnlageDao anlageDao;
    private SpielerDao spielerDao;
    private TurnierDao turnierDao;
    private SpielDao spielDao;
    public DatabaseHandler(Context context){

        //this.db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "database-name").build();
        this.db = AppDatabase.getInstance(context);
        anlageDao = db.anlageDao();
        spielerDao = db.spielerDao();
        turnierDao = db.turnierDao();
        spielDao = db.spielDao();
    }

    public DatabaseHandler() {
        this.db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
        anlageDao = db.anlageDao();
        spielerDao = db.spielerDao();
        turnierDao = db.turnierDao();
        spielDao = db.spielDao();
    }

    public void closeDb() {
        db.close();
    }
    //Spieler
    public Spieler addSpieler(String spieler){
        Spieler p = new Spieler(spieler);
        long spielerId = spielerDao.insert(p);
        return spielerDao.getByID(spielerId);
    }
    public void changeSpielerName(Spieler spieler){
        spielerDao.update(spieler);
    }
    public  void deleteSpieler(Spieler spieler){
        spielerDao.delete(spieler);
    }
    public  List<Spieler> getAllSpieler(){
        return spielerDao.getAll();
    }
    //Anlage
    public Anlage addAnlage(String name , int AnzahlBahnen){
        Anlage a = new Anlage(name,AnzahlBahnen);
        long AnlageID = anlageDao.insert(a);
        return anlageDao.getByID(AnlageID);

    }
    public  List<Anlage> getAllAnlage(){
        return anlageDao.getAll();
    }
    public void deleteAnlage(Anlage anlage){
        anlageDao.delete(anlage);
    }
    //Turnier
    public Turnier addTurnier(String Name,long Anlage){

        Turnier t = new Turnier(Name,Anlage);
        long TurnierID = turnierDao.insert(t);
        return turnierDao.getByID(TurnierID);
    }

    public  List<Turnier> getAllTurnier(){
        return turnierDao.getAll();
    }
    public void deleteTurnier(Turnier turnier){


    turnierDao.delete(turnier);
    }


    public void addPlayerToTurnier(long TurnierID,long PlayerID){
        TurnierSpielernCrossRef ts = new TurnierSpielernCrossRef(PlayerID,TurnierID);
        turnierDao.addPlayer(ts);

        Turnier t = turnierDao.getByID(TurnierID);
        Anlage a = anlageDao.getByID(t.AnlageID);

        for(int i = 1; i<= a.AnzahlBahnen;i++){
            //add -1 as not jet played score
            Spiel spiel=new Spiel(i,PlayerID,TurnierID,-1);
            spielDao.insert(spiel);
        }
    }

    public GameState getCurrentGameState(long TurnierID){
        TurnierMitSpieler tms= turnierDao.getTurnierMitSpielern(TurnierID);
        Anlage a = anlageDao.getByID(tms.turnier.AnlageID);
        //X: Player + 1 for Hole
        // Y: Bahnen + 2 for Names and Total
        String[][] gamestate = new String[tms.spieler.size()+1][a.AnzahlBahnen +2];

        //First row
        gamestate[0][0]="Hole";
        //loop fuer jeden Spieler: X
        for (int i = 0; i < tms.spieler.size(); i++) {
            gamestate[i+1][0] = tms.spieler.get(i).Name;
        }

        //first collumn
        //loop fuer jede bahn: Y
        for(int ii = 1; ii<= a.AnzahlBahnen;ii++){
            gamestate[0][ii]= Integer.toString(ii);
        }


        //core of array

        //loop fuer jede bahn: Y
        for(int y = 1; y<= a.AnzahlBahnen;y++){
            //loop fuer jeden Spieler: X
            for (int x = 0; x < tms.spieler.size(); x++) {
                Spiel spiel= spielDao.getByPrimaryKey(y, tms.spieler.get(x).SpielerID,TurnierID);
                if(spiel.SchlaegeNr==-1){
                    gamestate[x+1][y]="";
                }else {
                    gamestate[x+1][y]=Integer.toString(spiel.SchlaegeNr);
                }
            }
        }

        //last row
        gamestate[0][a.AnzahlBahnen+1]="Total";
        for (int x = 0; x < tms.spieler.size(); x++) {
            int total = spielDao.getSumOfSchlaege(tms.spieler.get(x).SpielerID,TurnierID);
            gamestate[x+1][a.AnzahlBahnen+1]=Integer.toString(total);
        }

        return new GameState(gamestate,tms.spieler);
    }

    public void updatePlay(long SpielerID, long TurnierID,int BahnNr, int AnzahlSchlaege){
        Spiel s =new Spiel(BahnNr,SpielerID,TurnierID,AnzahlSchlaege);
        spielDao.update(s);
    }

    public List<Spieler> currentWinner(long TurnierID){
        int currentBestScore =10000;
        List<Spieler> currenBestPlayer = new ArrayList<>();
        TurnierMitSpieler tms= turnierDao.getTurnierMitSpielern(TurnierID);
        for (int x = 0; x < tms.spieler.size(); x++) {
            int total = spielDao.getSumOfSchlaege(tms.spieler.get(x).SpielerID,TurnierID);
            if(total<currentBestScore){
                //new best player
                currenBestPlayer = new ArrayList<>();
                currenBestPlayer.add(tms.spieler.get(x));
                currentBestScore = total;
            }else if(total== currentBestScore){
                //new equel as best player
                currenBestPlayer.add(tms.spieler.get(x));
                currentBestScore = total;
            }
        }
        return currenBestPlayer;
    }


}
