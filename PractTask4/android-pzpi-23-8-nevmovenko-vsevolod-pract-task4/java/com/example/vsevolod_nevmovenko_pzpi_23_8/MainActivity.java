package com.example.pract4;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    EditText inputName, inputAge, inputFileData;
    Button savePrefsButton, loadPrefsButton, saveDbButton, loadDbButton, saveFileButton, loadFileButton;
    TextView displayResults;

    SharedPreferences sharedPreferences;
    SQLiteDatabase db;
    DBHelper dbHelper;
    static final String PREFS_NAME = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputName = findViewById(R.id.inputName);
        inputAge = findViewById(R.id.inputAge);
        inputFileData = findViewById(R.id.inputFileData);
        savePrefsButton = findViewById(R.id.savePrefsButton);
        loadPrefsButton = findViewById(R.id.loadPrefsButton);
        saveDbButton = findViewById(R.id.saveDbButton);
        loadDbButton = findViewById(R.id.loadDbButton);
        saveFileButton = findViewById(R.id.saveFileButton);
        loadFileButton = findViewById(R.id.loadFileButton);
        displayResults = findViewById(R.id.displayResults);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        savePrefsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToPreferences();
            }
        });

        loadPrefsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromPreferences();
            }
        });

        saveDbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDatabase();
            }
        });

        loadDbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromDatabase();
            }
        });

        saveFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile();
            }
        });

        loadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromFile();
            }
        });
    }

    private void saveToPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", inputName.getText().toString());
        editor.putString("age", inputAge.getText().toString());
        editor.apply();
        Toast.makeText(this, "Дані збережено у SharedPreferences", Toast.LENGTH_SHORT).show();
    }

    private void loadFromPreferences() {
        String name = sharedPreferences.getString("name", "No name defined");
        String age = sharedPreferences.getString("age", "No age defined");
        displayResults.setText("Name: " + name + "\nAge: " + age);
    }

    private void saveToDatabase() {
        ContentValues values = new ContentValues();
        values.put("name", inputName.getText().toString());
        values.put("age", Integer.parseInt(inputAge.getText().toString()));
        db.insert("users", null, values);
        Toast.makeText(this, "Дані збережено у SQLite Database", Toast.LENGTH_SHORT).show();
    }

    private void loadFromDatabase() {
        Cursor cursor = db.query("users", null, null, null, null, null, null);
        StringBuilder builder = new StringBuilder();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            int age = cursor.getInt(cursor.getColumnIndexOrThrow("age"));
            builder.append("Name: ").append(name).append(", Age: ").append(age).append("\n");
        }
        cursor.close();
        displayResults.setText(builder.toString());
    }

    private void saveToFile() {
        String fileData = inputFileData.getText().toString();
        try (FileOutputStream fos = openFileOutput("myfile.txt", Context.MODE_PRIVATE)) {
            fos.write(fileData.getBytes());
            Toast.makeText(this, "Дані збережено у файл", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        StringBuilder builder = new StringBuilder();
        try (FileInputStream fis = openFileInput("myfile.txt")) {
            int c;
            while ((c = fis.read()) != -1) {
                builder.append((char) c);
            }
            displayResults.setText(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "MyDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY, name TEXT, age INTEGER)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
