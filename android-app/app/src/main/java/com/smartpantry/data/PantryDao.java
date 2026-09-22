package com.smartpantry.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/** Data access for the pantry: the Create / Read / Update / Delete operations. */
@Dao
public interface PantryDao {

    @Insert
    long insert(PantryItem item);              // Create

    @Insert
    void insertAll(List<PantryItem> items);

    @Query("SELECT * FROM pantry_items ORDER BY name COLLATE NOCASE")
    List<PantryItem> getAll();                 // Read (list)

    @Query("SELECT * FROM pantry_items WHERE id = :id")
    PantryItem getById(long id);               // Read (single)

    @Update
    int update(PantryItem item);               // Update

    @Delete
    int delete(PantryItem item);               // Delete

    @Query("DELETE FROM pantry_items")
    void deleteAll();
}
