package com.example.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.example.notes.Adapter.NotesListAdapter;
import com.example.notes.DataBase.RoomDB;
import com.example.notes.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    RecyclerView recyclerView;
    FloatingActionButton fab_add;
    NotesListAdapter notesListAdapter;
    RoomDB database;
    List<Notes> notes = new ArrayList<>();
    SearchView searchView_home;
    Notes selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        fab_add = findViewById(R.id.fab_add);
        searchView_home = findViewById(R.id.searchView_home);
        Spinner spinner_priority_filter = findViewById(R.id.spinner_priority_filter);
        database = RoomDB.getInstance(this);

        notesListAdapter = new NotesListAdapter(this, new ArrayList<>(), notesClickListener);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(notesListAdapter);

        loadNotes();

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        spinner_priority_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPriority = parent.getItemAtPosition(position).toString();
                filterByPriority(selectedPriority);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                updateRecycler(notes);
            }
        });

    }

    private void loadNotes() {
        notes.clear();
        notes.addAll(database.mainDao().getAll());
        notesListAdapter.notifyDataSetChanged();
        Log.d("MainActivity", "Loaded notes. Total notes: " + notes.size());
    }

    private void filterByPriority(String priority) {
        List<Notes> filteredList = new ArrayList<>();
        for (Notes singleNote : notes) {
            if (singleNote.getPriority().equals(priority) || priority.equals("All")) {
                filteredList.add(singleNote);
            }
        }
        notesListAdapter.filterList(filteredList);
    }

    private void filter(String newText) {
        List<Notes> filteredList = new ArrayList<>();
        for (Notes singleNote : notes) {
            if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
                    || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(singleNote);
            }
        }
        notesListAdapter.filterList(filteredList);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Notes note = (Notes) data.getSerializableExtra("note");
            Log.d("MainActivity", "Received note from NotesTakerActivity: " + note.getTitle());

            if (requestCode == 101) {
                database.mainDao().insert(note);
                notes.add(note);
                notesListAdapter.notifyItemInserted(notes.size() - 1);
                Log.d("MainActivity", "Note added: " + note.getTitle() + note.getImageUrl() + note.getPriority());
                loadNotes();
            } else if (requestCode == 102) {
                database.mainDao().update(note.getID(), note.getTitle(), note.getNotes(), note.getImageUrl(), note.getPriority());
                int index = notes.indexOf(selectedNote);
                if (index != -1) {
                    notes.set(index, note);
                    notesListAdapter.notifyItemChanged(index);
                    Log.d("MainActivity", "Note updated: " + note.getTitle() + note.getImageUrl() + note.getPriority());
                }
                loadNotes();
            }
        }

    }
    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter = new NotesListAdapter(MainActivity.this, notes, notesClickListener);
        recyclerView.setAdapter(notesListAdapter);
    }
    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            intent.putExtra("old_notes", notes);
            startActivityForResult(intent, 102);

        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectedNote = notes;
            showPopUp (cardView);

        }
    };
    private void showPopUp(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();

    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId()==R.id.pin) {
            if (selectedNote.isPinned()) {
                database.mainDao().pin(selectedNote.getID(), false);
                Toast.makeText(MainActivity.this, "Unpinned", Toast.LENGTH_SHORT).show();
            } else {
                database.mainDao().pin(selectedNote.getID(), true);
                Toast.makeText(MainActivity.this, "Pinned", Toast.LENGTH_SHORT).show();
            }
            notes.clear();
            notes.addAll(database.mainDao().getAll());
            notesListAdapter.notifyDataSetChanged();
            return true;
        }
        if (item.getItemId() == R.id.delete) {
            database.mainDao().delete(selectedNote);
            int index = notes.indexOf(selectedNote);
            if (index != -1) {
                notes.remove(index);
                notesListAdapter.notifyItemRemoved(index);
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (item.getItemId() == R.id.change) {
            if (selectedNote != null) {
                notesClickListener.onClick(selectedNote);
            } else {
                Toast.makeText(MainActivity.this, "No note selected", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }
}
