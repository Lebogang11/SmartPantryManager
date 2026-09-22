package com.smartpantry.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.smartpantry.logic.Requirement;

/**
 * One ingredient line of a recipe (e.g. 200 g pasta). Many rows belong to one recipe:
 * recipe_ingredients.recipeId is a foreign key to recipes.id and rows are deleted with their recipe.
 */
@Entity(tableName = "recipe_ingredients",
        foreignKeys = @ForeignKey(entity = Recipe.class, parentColumns = "id", childColumns = "recipeId",
                onDelete = ForeignKey.CASCADE),
        indices = @Index("recipeId"))
public class RecipeIngredient implements Requirement {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long recipeId;

    @NonNull public String name = "";
    public double quantity;
    @NonNull public String unit = "pcs";

    public RecipeIngredient() { }

    @Ignore
    public RecipeIngredient(long recipeId, @NonNull String name, double quantity, @NonNull String unit) {
        this.recipeId = recipeId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    @Override public String getName() { return name; }
    @Override public double getQuantity() { return quantity; }
    @Override public String getUnit() { return unit; }
}
