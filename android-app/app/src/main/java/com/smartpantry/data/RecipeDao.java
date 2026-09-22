package com.smartpantry.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

/** Data access for the recipe collection (read-only after seeding). */
@Dao
public interface RecipeDao {

    @Insert
    long insertRecipe(Recipe recipe);

    @Insert
    void insertIngredients(List<RecipeIngredient> ingredients);

    @Query("SELECT COUNT(*) FROM recipes")
    int count();

    @Transaction
    @Query("SELECT * FROM recipes ORDER BY name COLLATE NOCASE")
    List<RecipeWithIngredients> getAllWithIngredients();

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id")
    RecipeWithIngredients getById(long id);
}
