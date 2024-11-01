package com.example.pract23;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Activity1", "onCreate called");

        EditText editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(view -> {

            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        });

        if (savedInstanceState != null) {
            String savedText = savedInstanceState.getString("savedText");
            editText.setText(savedText);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Activity1", "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Activity1", "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Activity1", "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Activity1", "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Activity1", "onDestroy called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        EditText editText = findViewById(R.id.editText);
        outState.putString("savedText", editText.getText().toString());
        Log.d("Activity1", "onSaveInstanceState called");
    }
}
