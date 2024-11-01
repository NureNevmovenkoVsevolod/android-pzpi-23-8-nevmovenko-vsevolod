package com.example.pract23;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.d("Activity2", "onCreate called");

        Button finishButton = findViewById(R.id.buttonFinish);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Activity2", "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Activity2", "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Activity2", "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Activity2", "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Activity2", "onDestroy called");
    }
}
