package com.example.saving_test.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.saving_test.Fragments.ViewPagerAdapter;
import com.example.saving_test.R;
import com.example.saving_test.database.DatabaseHandler;
import com.example.saving_test.database.entity.Turnier;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private DatabaseHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new DatabaseHandler(this);


        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Spieler"); break;
                case 1: tab.setText("Anlage"); break;
                case 2: tab.setText("Turnier"); break;
            }
        }).attach();

        Button buttonGoToSecondActivity = findViewById(R.id.bottomButton);
        buttonGoToSecondActivity.setOnClickListener(v -> showTurnierSelectionDialog());

    }
    public DatabaseHandler getDatabaseHandler() {
        return dbHandler;
    }

    private void showTurnierSelectionDialog() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            List<Turnier> allTurniere = dbHandler.getAllTurnier(); // Fetch in background

            if (allTurniere == null || allTurniere.isEmpty()) {
                mainHandler.post(() ->
                        Toast.makeText(this, "No Turniere found", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            mainHandler.post(() -> {
                Spinner spinner = new Spinner(this);
                ArrayAdapter<Turnier> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item, allTurniere);
                spinner.setAdapter(adapter);

                new AlertDialog.Builder(this)
                        .setTitle("Select Turnier")
                        .setView(spinner)
                        .setPositiveButton("Go", (dialog, which) -> {
                            Turnier selectedTurnier = (Turnier) spinner.getSelectedItem();
                            if (selectedTurnier != null) {
                                Intent intent = new Intent(MainActivity.this, GridActivity.class);
                                intent.putExtra("TURNIER_ID", selectedTurnier.TurnierID);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        });
    }

}
