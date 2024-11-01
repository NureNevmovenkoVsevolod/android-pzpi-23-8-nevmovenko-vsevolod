package com.example.pract21;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Обробник для кнопки 1
    public void onButton1Click(View view) {
        Toast.makeText(this, "Button 1 Clicked", Toast.LENGTH_SHORT).show();
    }

    // Обробник для кнопки 2
    public void onButton2Click(View view) {
        Toast.makeText(this, "Button 2 Clicked", Toast.LENGTH_SHORT).show();
    }

    // Обробник для кнопки 3
    public void onButton3Click(View view) {
        Toast.makeText(this, "Button 3 Clicked", Toast.LENGTH_SHORT).show();
    }
}
