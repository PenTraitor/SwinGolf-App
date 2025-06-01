package com.example.saving_test.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saving_test.R;
import com.example.saving_test.database.DatabaseHandler;
import com.example.saving_test.database.entity.*;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private DatabaseHandler dbHandler;

    EditText editTextPlayerName, editTextAnlageName, editTextTurnierName;
    Spinner spinnerAnlageForTurnier, spinnerSelectPlayer, spinnerSelectTurnier,spinnerSelectTurnier2;
    Button buttonAddPlayer, buttonAddAnlage, buttonAddTurnier, buttonAddPlayerToTurnier;

    NumberPicker numberPickerAnlageNumber;


    ArrayAdapter<Anlage> AnlageAdapter;
    ArrayAdapter<Spieler> spielerAdapter;
    ArrayAdapter<Turnier> turnierAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DatabaseHandler(this);

        editTextPlayerName = findViewById(R.id.editTextPlayerName);
        editTextAnlageName = findViewById(R.id.editTextAnlageName);
        editTextTurnierName = findViewById(R.id.editTextTurnierName);

        spinnerAnlageForTurnier = findViewById(R.id.spinnerAnlageForTurnier);
        spinnerSelectPlayer = findViewById(R.id.spinnerSelectPlayer);
        spinnerSelectTurnier = findViewById(R.id.spinnerSelectTurnier);
        spinnerSelectTurnier2 = findViewById(R.id.spinnerSelectTurnier2);

        buttonAddPlayer = findViewById(R.id.buttonAddPlayer);
        buttonAddAnlage = findViewById(R.id.buttonAddAnlage);
        buttonAddTurnier = findViewById(R.id.buttonAddTurnier);
        buttonAddPlayerToTurnier = findViewById(R.id.buttonAddPlayerToTurnier);

        Button buttonGoToSecondActivity = findViewById(R.id.buttonGoToGridActivity);
        buttonGoToSecondActivity.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GridActivity.class);
            Turnier turnier = (Turnier) spinnerSelectTurnier.getSelectedItem();
            if (turnier != null) {
                intent.putExtra("TURNIER_ID", turnier.TurnierID);
                startActivity(intent);
            }
        });


        numberPickerAnlageNumber = findViewById(R.id.numberPickerAnlageNumber);
        numberPickerAnlageNumber.setMinValue(1);
        numberPickerAnlageNumber.setMaxValue(18);

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Spieler> allSpieler = dbHandler.getAllSpieler();
            List<Anlage> allAnlage = dbHandler.getAllAnlage();
            List<Turnier> allTurnier = dbHandler.getAllTurnier();

            // Switch back to the UI thread to update the views
            runOnUiThread(() -> {
                spielerAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, allSpieler);
                spielerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                AnlageAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, allAnlage);
                AnlageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                turnierAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, allTurnier);
                turnierAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerSelectPlayer.setAdapter(spielerAdapter);
                spinnerAnlageForTurnier.setAdapter(AnlageAdapter);
                spinnerSelectTurnier.setAdapter(turnierAdapter);
                spinnerSelectTurnier2.setAdapter(turnierAdapter);
            });
        });

        buttonAddPlayer.setOnClickListener(v -> {
            String name = editTextPlayerName.getText().toString();
            if (!name.isEmpty()) {
                Executor executorplayer = Executors.newSingleThreadExecutor();
                executorplayer.execute(() -> {
                    Spieler s = dbHandler.addSpieler(name); // DB call off main thread

                    runOnUiThread(() -> {
                        spielerAdapter.add(s);
                        spielerAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Player added", Toast.LENGTH_SHORT).show();
                    });
                });
            }
        });


        buttonAddAnlage.setOnClickListener(v -> {
            String name = editTextAnlageName.getText().toString().trim();
            int number = numberPickerAnlageNumber.getValue();

            if (!name.isEmpty()) {
                executor.execute(() -> {
                    Anlage a = dbHandler.addAnlage(name, number);
                    runOnUiThread(() -> {
                        AnlageAdapter.add(a);
                        AnlageAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "Anlage added", Toast.LENGTH_SHORT).show();
                    });
                });
            }
        });

        buttonAddTurnier.setOnClickListener(v -> {
            String name = editTextTurnierName.getText().toString().trim();
            Anlage anlage = (Anlage) spinnerAnlageForTurnier.getSelectedItem();

            if (!name.isEmpty() && anlage != null) {
                executor.execute(() -> {
                    Turnier t = dbHandler.addTurnier(name, anlage.AnlageID);
                    runOnUiThread(() -> {
                        turnierAdapter.add(t);
                        turnierAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "Turnier added", Toast.LENGTH_SHORT).show();
                    });
                });
            }
        });

        buttonAddPlayerToTurnier.setOnClickListener(v -> {
            Spieler spieler = (Spieler) spinnerSelectPlayer.getSelectedItem();
            Turnier turnier = (Turnier) spinnerSelectTurnier.getSelectedItem();

            if (spieler != null && turnier != null) {
                executor.execute(() -> {
                    dbHandler.addPlayerToTurnier(turnier.TurnierID, spieler.SpielerID);
                    runOnUiThread(() -> Toast.makeText(this, "Player added to Turnier", Toast.LENGTH_SHORT).show());
                });
            }
        });
    }
}
