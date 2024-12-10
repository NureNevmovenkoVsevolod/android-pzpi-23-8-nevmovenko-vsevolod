package com.example.pract31;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler(Looper.getMainLooper());

        // Обробник AlertDialog
        Button showDialogButton = findViewById(R.id.showDialogButton);
        showDialogButton.setOnClickListener(v -> new AlertDialog.Builder(MainActivity.this)
                .setTitle("Dialog")
                .setMessage("This is an AlertDialog example.")
                .setPositiveButton("OK", (dialog, which) -> {
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                })
                .show());

        // Обробник DatePickerDialog
        Button showDatePickerButton = findViewById(R.id.showDatePickerButton);
        showDatePickerButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                    (view, year, month, dayOfMonth) -> {
                    }, 2023, 8, 1);
            datePickerDialog.show();
        });

        // Обробник Custom Dialog
        Button showCustomDialogButton = findViewById(R.id.showCustomDialogButton);
        showCustomDialogButton.setOnClickListener(v -> {
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_dialog, null);

            new AlertDialog.Builder(MainActivity.this)
                    .setView(dialogView)
                    .setPositiveButton("OK", (dialog, id) -> {
                    })
                    .setNegativeButton("Cancel", (dialog, id) -> {
                    })
                    .create()
                    .show();
        });

        // Обробник Handler
        Button startHandlerButton = findViewById(R.id.startHandlerButton);
        TextView handlerMessageTextView = findViewById(R.id.handlerMessageTextView);
        startHandlerButton.setOnClickListener(v -> {
            handler.postDelayed(() -> handlerMessageTextView.setText("Handler executed after delay"), 2000);

            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(() -> handlerMessageTextView.setText("Updated from background thread"));
            }).start();
        });
    }
}
