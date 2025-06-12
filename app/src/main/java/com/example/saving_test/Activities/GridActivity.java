package com.example.saving_test.Activities;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saving_test.R;
import com.example.saving_test.database.DatabaseHandler;
import com.example.saving_test.database.dao.GameState;
import com.example.saving_test.database.entity.Spieler;
import com.example.saving_test.database.entity.Turnier;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class GridActivity extends AppCompatActivity {
    private DatabaseHandler dbHandler;
    private long turnierID;
    private int colCount;
    private List<Spieler> spielerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHandler = new DatabaseHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        Button buttonGetWinner = findViewById(R.id.winnerButton);
        buttonGetWinner.setOnClickListener(v -> getCurrentWinnerDialog());


        turnierID = getIntent().getLongExtra("TURNIER_ID", -1);


        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            GameState state= dbHandler.getCurrentGameState(turnierID);

            String[][] gameState = state.grid;
            spielerList= state.spieler;
            //  Update UI on main thread
            runOnUiThread(() -> setGrid(gameState));
        });
    }

    private void getCurrentWinnerDialog() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            List<Spieler> allWinner = dbHandler.currentWinner(turnierID); // Fetch in background
            String winner = allWinner
                    .stream()
                    .map(a -> String.valueOf(a.Name))
                    .collect(Collectors.joining(","));

            if (allWinner == null || allWinner.isEmpty()) {
                mainHandler.post(() ->
                        Toast.makeText(this, "No Winner found", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            mainHandler.post(() -> {
                // 1. Instantiate an AlertDialog.Builder with its constructor.
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                // 2. Chain together various setter methods to set the dialog characteristics.
                builder.setMessage(winner )
                        .setTitle("Current Winner");

                // 3. Get the AlertDialog.
                AlertDialog dialog = builder.create();
                dialog.show();
            });
        });
    }

    private void setGrid(String[][] gameState) {
        TableLayout tableLayout = findViewById(R.id.tableLayoutGameState);

        // Clear previous views if any
        tableLayout.removeAllViews();

        if (gameState.length == 0) return;

        int rowCount = gameState.length;
        colCount = gameState[0].length;

        for (int col = 0; col < colCount; col++) {
            TableRow tableRow = new TableRow(this);
            for (int row = 0; row < rowCount; row++) {
                TextView textView = new TextView(this);
                textView.setText(gameState[row][col]);  // Inverted access
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                textView.setPadding(0, 0, 0, 0);
                textView.setBackgroundResource(R.drawable.grid_cell_background);

                // Inverted tag: [col, row]
                textView.setTag(new int[]{col, row});

                textView.setOnClickListener(cellClickListener);
                tableRow.addView(textView);
            }
            tableLayout.addView(tableRow);
        }
    }


    // Cell click handler
    private final View.OnClickListener cellClickListener = view -> {
        TextView cell = (TextView) view;
        int[] pos = (int[]) cell.getTag();
        int row = pos[0];
        int col = pos[1];


        if(row >0 && col >0 && colCount -1 >row){
            Spieler spieler  = spielerList.get(col-1);
            onCellClicked(spieler,row);
        }

    };

    // Your custom function when a cell is clicked
    private void onCellClicked(Spieler spieler, int loch) {
        Toast.makeText(this, "Clicked cell (" + spieler + "," + loch + "): " , Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter number for cell (" + spieler + ", Loch" + loch + ")");

        // Create input field
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        //input.setText(currentValue);
        input.setSelectAllOnFocus(true);
        input.setPadding(50, 40, 50, 40);

        builder.setView(input);

        // Set buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String valueStr = input.getText().toString().trim();
            if (!valueStr.isEmpty()) {
                try {
                    int enteredValue = Integer.parseInt(valueStr);
                    onNumberConfirmed(spieler, loch, enteredValue);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
    private void onNumberConfirmed(Spieler spieler, int loch, int number) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {

            dbHandler.updatePlay(spieler.SpielerID,turnierID,loch,number);

            GameState state= dbHandler.getCurrentGameState(turnierID);

            String[][] gameState = state.grid;
            spielerList= state.spieler;
            //  Update UI on main thread
            runOnUiThread(() -> setGrid(gameState));
        });
    }

}
