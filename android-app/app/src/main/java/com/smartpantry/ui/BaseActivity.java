package com.smartpantry.ui;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smartpantry.R;

/**
 * Shared behaviour for every screen: the bottom navigation bar (uses Intents to switch between
 * the three top-level screens), safe UI-thread posting for background results, and the toolbar Up button.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /** Wires the bottom navigation bar and marks the given item as selected. */
    protected void setupBottomNav(int selectedItemId) {
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(selectedItemId);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedItemId) return true;
            Class<?> target = id == R.id.nav_pantry ? PantryListActivity.class
                    : id == R.id.nav_cook ? SuggestedRecipesActivity.class
                    : SettingsActivity.class;
            Intent intent = new Intent(this, target);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
            return true;
        });
    }

    /** Runs on the UI thread, but only if the screen is still alive (background work may finish late). */
    protected void ui(@NonNull Runnable action) {
        if (!isFinishing() && !isDestroyed()) runOnUiThread(action);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
