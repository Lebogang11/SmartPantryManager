package com.smartpantry.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** First-run data: pre-loads the recipe collection and can fill the pantry with pre-saved ingredients. */
public final class DatabaseSeeder {

    private DatabaseSeeder() { }

    /** Writes the 20 recipes the first time the app runs (does nothing if they already exist). Call off the main thread. */
    public static void ensureRecipesSeeded(AppDatabase db) {
        final RecipeDao dao = db.recipeDao();
        if (dao.count() > 0) return;
        db.runInTransaction(() -> {
            for (SeedData.SeedRecipe s : SeedData.RECIPES) {
                long recipeId = dao.insertRecipe(new Recipe(s.name, s.emoji, s.minutes, s.servings, String.join("\n", s.steps)));
                List<RecipeIngredient> rows = new ArrayList<>();
                for (SeedData.Ing i : s.ingredients) rows.add(new RecipeIngredient(recipeId, i.name, i.quantity, i.unit));
                dao.insertIngredients(rows);
            }
        });
    }

    /** Replaces the pantry with 18 sample ingredients whose expiry dates are relative to today. Call off the main thread. */
    public static void loadDemoPantry(AppDatabase db) {
        final LocalDate today = LocalDate.now();
        final List<PantryItem> items = new ArrayList<>();
        for (SeedData.DemoItem d : SeedData.DEMO_PANTRY) {
            String expiry = d.expiryOffsetDays == null ? null : today.plusDays(d.expiryOffsetDays).toString();
            items.add(new PantryItem(d.name, d.quantity, d.unit, expiry));
        }
        db.runInTransaction(() -> {
            db.pantryDao().deleteAll();
            db.pantryDao().insertAll(items);
        });
    }

    /** Removes every pantry item (recipes are kept). Call off the main thread. */
    public static void clearPantry(AppDatabase db) { db.pantryDao().deleteAll(); }
}
