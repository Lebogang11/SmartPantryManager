package com.smartpantry.data;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/** Represents a recipe and its ingredients. */
public class RecipeWithIngredients {

    @Embedded
    public Recipe recipe;

    @Relation(parentColumn = "id", entityColumn = "recipeId")
    public List<RecipeIngredient> ingredients;
}
