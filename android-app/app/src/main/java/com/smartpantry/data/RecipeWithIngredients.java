package com.smartpantry.data;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/** A recipe together with all of its ingredient rows (one-to-many relation loaded by Room). */
public class RecipeWithIngredients {

    @Embedded
    public Recipe recipe;

    @Relation(parentColumn = "id", entityColumn = "recipeId")
    public List<RecipeIngredient> ingredients;
}
