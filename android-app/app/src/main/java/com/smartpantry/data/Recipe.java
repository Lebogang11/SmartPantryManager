package com.smartpantry.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Arrays;
import java.util.List;

/** A recipe in the pre-loaded collection. Its ingredients live in the recipe_ingredients table. */
@Entity(tableName = "recipes")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull public String name = "";
    @NonNull public String emoji = "";
    public int minutes;
    public int servings;

    /** Preparation steps, one per line. */
    @NonNull public String steps = "";

    public Recipe() { }

    @Ignore
    public Recipe(@NonNull String name, @NonNull String emoji, int minutes, int servings, @NonNull String steps) {
        this.name = name;
        this.emoji = emoji;
        this.minutes = minutes;
        this.servings = servings;
        this.steps = steps;
    }

    @Ignore
    public List<String> stepList() { return Arrays.asList(steps.split("\n")); }
}
