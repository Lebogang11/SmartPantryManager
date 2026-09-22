package com.smartpantry.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The single Room database for the app
 * Room forbids database access on the main thread, so all calls run on the {@link #io()} executor.
 */
@Database(entities = {PantryItem.class, Recipe.class, RecipeIngredient.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PantryDao pantryDao();

    public abstract RecipeDao recipeDao();

    private static volatile AppDatabase instance;

    /** One background thread keeps database operations in the order they were requested. */
    private static final ExecutorService IO = Executors.newSingleThreadExecutor();

    public static AppDatabase get(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "smart_pantry.db")
                            .build();
                }
            }
        }
        return instance;
    }

    public static ExecutorService io() { return IO; }
}
