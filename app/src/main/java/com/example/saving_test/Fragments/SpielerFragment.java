package com.example.saving_test.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.saving_test.Activities.MainActivity;
import com.example.saving_test.R;
import com.example.saving_test.database.DatabaseHandler;
import com.example.saving_test.database.entity.Spieler;


import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SpielerFragment extends Fragment {

    private DatabaseHandler dbHandler;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof MainActivity) {
            dbHandler = ((MainActivity) getActivity()).getDatabaseHandler();
        }


        View view = inflater.inflate(R.layout.fragment_spieler, container, false);

        Button btnAdd = view.findViewById(R.id.btnAddPlayer);
        Button btnChange = view.findViewById(R.id.btnChangePlayer);
        Button btnDelete = view.findViewById(R.id.btnDeletePlayer);

        btnAdd.setOnClickListener(v -> showAddPlayerDialog());
        btnChange.setOnClickListener(v -> showChangePlayerDialog());
        btnDelete.setOnClickListener(v -> showDeletePlayerDialog());

        return view;
    }

    private void showAddPlayerDialog() {
        EditText input = new EditText(getContext());
        input.setHint("Enter player name");

        new AlertDialog.Builder(getContext())
                .setTitle("Add Player")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    Executor executorplayer = Executors.newSingleThreadExecutor();
                    executorplayer.execute(() -> {
                        dbHandler.addSpieler(name); // DB call off main thread
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showChangePlayerDialog() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            List<Spieler> allSpieler = dbHandler.getAllSpieler(); // now off main thread

            if (allSpieler == null || allSpieler.isEmpty()) {
                mainHandler.post(() ->
                        Toast.makeText(getContext(), "No players to change", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            mainHandler.post(() -> {
                // UI code on main thread
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(50, 40, 50, 10);

                Spinner spinner = new Spinner(getContext());
                ArrayAdapter<Spieler> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item, allSpieler);
                spinner.setAdapter(adapter);
                layout.addView(spinner);

                EditText input = new EditText(getContext());
                input.setHint("New name");
                layout.addView(input);

                new AlertDialog.Builder(getContext())
                        .setTitle("Change Player Name")
                        .setView(layout)
                        .setPositiveButton("Change", (dialog, which) -> {
                            String newName = input.getText().toString().trim();
                            Spieler oldPlayer = (Spieler) spinner.getSelectedItem();

                            if (!newName.isEmpty()) {
                                oldPlayer.Name = newName;

                                // Perform DB update off main thread
                                executor.execute(() -> {
                                    dbHandler.changeSpielerName(oldPlayer);
                                });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        });
    }


    private void showDeletePlayerDialog() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            List<Spieler> allSpieler = dbHandler.getAllSpieler();

            if (allSpieler == null || allSpieler.isEmpty()) {
                mainHandler.post(() ->
                        Toast.makeText(getContext(), "No players to delete", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            mainHandler.post(() -> {
                Spinner spinner = new Spinner(getContext());
                ArrayAdapter<Spieler> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item, allSpieler);
                spinner.setAdapter(adapter);

                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Player")
                        .setView(spinner)
                        .setPositiveButton("Delete", (dialog, which) -> {
                            Spieler selectedPlayer = (Spieler) spinner.getSelectedItem();

                            executor.execute(() -> {
                                dbHandler.deleteSpieler(selectedPlayer);
                            });

                            Toast.makeText(getContext(), "Player deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        });
    }

}
