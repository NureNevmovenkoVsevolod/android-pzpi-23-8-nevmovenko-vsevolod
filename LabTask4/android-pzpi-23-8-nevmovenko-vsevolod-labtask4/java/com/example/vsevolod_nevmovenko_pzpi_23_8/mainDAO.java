package com.example.notes.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.notes.Models.Notes;

import java.util.List;

@Dao
public interface mainDAO {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert (Notes notes);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Notes> getAll();

    @Query("UPDATE notes SET title = :title, notes = :notes, priority = :priority, imageUrl = :imageUrl where ID = :id")
    void update (int id, String title, String notes, String priority, String imageUrl);

    @Delete
    void delete (Notes notes);

    @Query("UPDATE notes SET pinned = :pin WHERE ID = :id")
    void pin(int id, boolean pin);
}
