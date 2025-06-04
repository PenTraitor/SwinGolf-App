package com.example.saving_test.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.saving_test.Activities.MainActivity;
import com.example.saving_test.R;
import com.example.saving_test.database.DatabaseHandler;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AnlageFragment extends Fragment {

    private DatabaseHandler dbHandler;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof MainActivity) {
            dbHandler = ((MainActivity) getActivity()).getDatabaseHandler();
        }


        View view = inflater.inflate(R.layout.fragment_anlage, container, false);

        Button btnAdd = view.findViewById(R.id.btnAddAnlage);

        btnAdd.setOnClickListener(v -> showAddAnlageDialog());

        return view;
    }

    private void showAddAnlageDialog() {

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        // Text input
        EditText input = new EditText(getContext());
        input.setHint("Anlage Name");
        layout.addView(input);

        // NumberPicker
        NumberPicker numberPicker = new NumberPicker(getContext());
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(18);
        layout.addView(numberPicker);

        new AlertDialog.Builder(getContext())
                .setTitle("Add Anlage")
                .setView(layout)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    int selectedNumber = numberPicker.getValue();

                    if (!name.isEmpty()) {
                        Executor executor = Executors.newSingleThreadExecutor();
                        executor.execute(() -> {
                            dbHandler.addAnlage(name, selectedNumber);
                        });
                    } else {
                        Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}