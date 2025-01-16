package com.example.notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.notes.Models.Notes;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTakerActivity extends AppCompatActivity {
    EditText editText_title, editText_notes;
    ImageView imageView_save, imageView_selected;
    Notes notes;
    private static final int REQUEST_CODE_PERMISSIONS = 1001;
    boolean isOldNote = false;
    private static final int PICK_IMAGE = 1;
    String imageUrl = "";

    String image_url = "";


    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_PERMISSIONS);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);
        Log.d("NotesTakerActivity", "Activity created.");
        imageView_save = findViewById(R.id.imageView_save);
        editText_notes = findViewById(R.id.editText_notes);
        editText_title = findViewById(R.id.editText_title);
        imageView_selected = findViewById(R.id.imageView_selected);
        Button button_select_image = findViewById(R.id.button_select_image);
        Spinner spinner_priority = findViewById(R.id.spinner_priority);

        notes = new Notes();
        try {
            notes = (Notes) getIntent().getSerializableExtra("old_notes");
            editText_title.setText(notes.getTitle());
            editText_notes.setText(notes.getNotes());
            spinner_priority.setSelection(getPriorityIndex(notes.getPriority()));
            imageUrl = notes.getImageUrl();
            if (!imageUrl.isEmpty()) {
                imageView_selected.setVisibility(View.VISIBLE);
                Glide.with(this).load(imageUrl).into(imageView_selected);
                button_select_image.setVisibility(View.GONE);
            }
            isOldNote = true;
            Log.d("NotesTakerActivity", "Loaded existing note: " + notes.getTitle());
        } catch (Exception e){
            e.printStackTrace();
            Log.e("NotesTakerActivity", "Error loading note: " + e.getMessage());
        }

        button_select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editText_title.getText().toString();
                String description = editText_notes.getText().toString();
                String priority = spinner_priority.getSelectedItem().toString();
                String imageSelected = image_url;
                if(description.isEmpty()) {
                    Toast.makeText(NotesTakerActivity.this, "Please, enter description", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                Date date = new Date();

                if (!isOldNote) {
                    notes = new Notes();
                }

                notes.setTitle(title);
                notes.setNotes(description);
                notes.setDate(formatter.format(date));
                notes.setPriority(priority);
                notes.setImageUrl(imageSelected);
                Log.d("NotesTakerActivity", "Saving note: " + notes.getTitle() + " with image URL: " + imageSelected);

                Intent intent = new Intent();
                intent.putExtra("note", notes);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    image_url = selectedImageUri.toString(); // Сохраняем URI
                    imageView_selected.setVisibility(View.VISIBLE);
                    Glide.with(this).load(selectedImageUri).into(imageView_selected);
                    Log.d("NotesTakerActivity", "Image loaded successfully");
                } catch (Exception e) {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    Log.e("NotesTakerActivity", "Error loading image: " + e.getMessage());
                }
            } else {
                Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int getPriorityIndex(String priority) {
        switch (priority) {
            case "High":
                return 0;
            case "Medium":
                return 1;
            case "Low":
                return 2;
            default:
                return 0;
        }
    }
}
