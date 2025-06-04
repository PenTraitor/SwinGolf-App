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
import com.example.saving_test.database.entity.Anlage;
import com.example.saving_test.database.entity.Spieler;
import com.example.saving_test.database.entity.Turnier;


import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TurnierFragment extends Fragment {

    private DatabaseHandler dbHandler;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof MainActivity) {
            dbHandler = ((MainActivity) getActivity()).getDatabaseHandler();
        }


        View view = inflater.inflate(R.layout.fragment_turnier, container, false);

        Button btnAdd = view.findViewById(R.id.btnAddTurnier);
        Button btnAddPlayer = view.findViewById(R.id.btnAddPlayerToTurnier);


        btnAdd.setOnClickListener(v -> showAddTurnierDialog());
        btnAddPlayer.setOnClickListener(v -> showAddPlayerDialog());


        return view;
    }

    private void showAddTurnierDialog() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            List<Anlage> allAnlage= dbHandler.getAllAnlage();


            if (allAnlage == null || allAnlage.isEmpty()) {
                mainHandler.post(() ->
                        Toast.makeText(getContext(), "No Anlage found", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            mainHandler.post(() -> {
                // UI code on main thread
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(50, 40, 50, 10);

                Spinner spinner = new Spinner(getContext());
                ArrayAdapter<Anlage> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item, allAnlage);
                spinner.setAdapter(adapter);
                layout.addView(spinner);

                EditText input = new EditText(getContext());
                input.setHint("Name");
                layout.addView(input);

                new AlertDialog.Builder(getContext())
                        .setTitle("Add Turnier")
                        .setView(layout)
                        .setPositiveButton("Add", (dialog, which) -> {
                            String name = input.getText().toString().trim();
                            Anlage anlage = (Anlage) spinner.getSelectedItem();

                            if (!name.isEmpty()) {
                                // Perform DB update off main thread
                                executor.execute(() -> {
                                    dbHandler.addTurnier(name,anlage.AnlageID);
                                });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        });
    }

    private void showAddPlayerDialog() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            List<Spieler> allSpieler= dbHandler.getAllSpieler();
            List<Turnier> allTurnier= dbHandler.getAllTurnier();


            if (allSpieler == null || allSpieler.isEmpty()) {
                mainHandler.post(() ->
                        Toast.makeText(getContext(), "No Player found", Toast.LENGTH_SHORT).show()
                );
                return;
            }
            if (allTurnier == null || allTurnier.isEmpty()) {
                mainHandler.post(() ->
                        Toast.makeText(getContext(), "No Turnier found", Toast.LENGTH_SHORT).show()
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

                Spinner spinner2 = new Spinner(getContext());
                ArrayAdapter<Turnier> adapter2 = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item, allTurnier);
                spinner2.setAdapter(adapter2);
                layout.addView(spinner2);


                new AlertDialog.Builder(getContext())
                        .setTitle("Add Turnier")
                        .setView(layout)
                        .setPositiveButton("Add", (dialog, which) -> {

                            Spieler spieler = (Spieler) spinner.getSelectedItem();
                            Turnier turnier = (Turnier) spinner2.getSelectedItem();

                                // Perform DB update off main thread
                                executor.execute(() -> {
                                    dbHandler.addPlayerToTurnier(turnier.TurnierID,spieler.SpielerID);
                                });

                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        });
    }




}
