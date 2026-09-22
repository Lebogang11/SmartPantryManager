package com.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.smartpantry.R;
import com.smartpantry.adapter.RecipeAdapter;
import com.smartpantry.data.AppDatabase;
import com.smartpantry.data.DatabaseSeeder;
import com.smartpantry.data.PantryItem;
import com.smartpantry.data.RecipeWithIngredients;
import com.smartpantry.logic.MatchResult;
import com.smartpantry.logic.RecipeMatcher;
import com.smartpantry.util.Formatters;
import com.smartpantry.util.SettingsManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Suggested Recipes screen. It loads the pantry and every recipe, runs the STRICT matching rule
 * (RecipeMatcher.evaluate) on each recipe and lists only the recipes whose every ingredient is
 * covered. A separate tab shows the optional "Almost there" recipes (exactly one ingredient short);
 * those are never mixed into the strict list.
 */
public class SuggestedRecipesActivity extends BaseActivity implements RecipeAdapter.Listener {

    private AppDatabase db;
    private SettingsManager settings;
    private RecipeAdapter adapter;

    private final List<RecipeAdapter.Row> readyRows = new ArrayList<>();
    private final List<RecipeAdapter.Row> almostRows = new ArrayList<>();

    private TabLayout tabs;
    private RecyclerView recycler;
    private View emptyState;
    private TextView emptyTitle, emptyMessage, almostNote, footnote;
    private Button emptyAddButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        setSupportActionBar(findViewById(R.id.toolbar));

        db = AppDatabase.get(this);
        settings = new SettingsManager(this);

        tabs = findViewById(R.id.tabLayout);
        tabs.addTab(tabs.newTab().setText("Ready to cook"));
        tabs.addTab(tabs.newTab().setText("Almost there"));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) { showSelectedTab(); }
            @Override public void onTabUnselected(TabLayout.Tab tab) { }
            @Override public void onTabReselected(TabLayout.Tab tab) { }
        });

        recycler = findViewById(R.id.recyclerRecipes);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(this);
        recycler.setAdapter(adapter);

        emptyState = findViewById(R.id.emptyState);
        emptyTitle = findViewById(R.id.emptyTitle);
        emptyMessage = findViewById(R.id.emptyMessage);
        emptyAddButton = findViewById(R.id.buttonEmptyAdd);
        almostNote = findViewById(R.id.textAlmostNote);
        footnote = findViewById(R.id.textFootnote);
        emptyAddButton.setOnClickListener(v -> startActivity(new Intent(this, AddEditIngredientActivity.class)));

        setupBottomNav(R.id.nav_cook);
    }

    /** Re-run the matching every time the screen becomes visible, so it always reflects the current pantry. */
    @Override
    protected void onResume() {
        super.onResume();
        AppDatabase.io().execute(() -> {
            DatabaseSeeder.ensureRecipesSeeded(db);
            final List<PantryItem> pantry = db.pantryDao().getAll();
            final List<RecipeWithIngredients> recipes = db.recipeDao().getAllWithIngredients();
            final LocalDate today = LocalDate.now();
            final boolean ignoreExpired = settings.isIgnoreExpired();
            final int soonDays = settings.getAlertDays();

            final List<RecipeAdapter.Row> ready = new ArrayList<>(), almost = new ArrayList<>();
            for (RecipeWithIngredients r : recipes) {
                MatchResult m = RecipeMatcher.evaluate(r.recipe.id, r.ingredients, pantry, ignoreExpired, today, soonDays);
                if (m.isReady()) ready.add(new RecipeAdapter.Row(r, m));          // every ingredient covered
                else if (m.isAlmostThere()) almost.add(new RecipeAdapter.Row(r, m)); // exactly one short (bonus list)
            }
            // Recipes that use up expiring ingredients come first, then quicker recipes.
            ready.sort(Comparator.<RecipeAdapter.Row>comparingInt(x -> -x.result.expiringUsed.size())
                    .thenComparingInt(x -> x.recipe.recipe.minutes));
            almost.sort(Comparator.comparingInt(x -> x.recipe.recipe.minutes));

            int expired = 0;
            for (PantryItem p : pantry) if (RecipeMatcher.isExpired(p, today)) expired++;
            final int expiredCount = ignoreExpired ? expired : 0;

            ui(() -> {
                readyRows.clear(); readyRows.addAll(ready);
                almostRows.clear(); almostRows.addAll(almost);
                tabs.getTabAt(0).setText("Ready to cook (" + readyRows.size() + ")");
                tabs.getTabAt(1).setText("Almost there (" + almostRows.size() + ")");
                getSupportActionBar().setSubtitle(readyRows.size() + " of " + recipes.size() + " recipes ready");
                footnote.setText(expiredCount > 0 ? Formatters.plural(expiredCount, "expired item") + " ignored. Change this in Settings." : "");
                footnote.setVisibility(expiredCount > 0 ? View.VISIBLE : View.GONE);
                showSelectedTab();
            });
        });
    }

    private void showSelectedTab() {
        boolean almostTab = tabs.getSelectedTabPosition() == 1;
        List<RecipeAdapter.Row> rows = almostTab ? almostRows : readyRows;
        adapter.submit(rows, almostTab);
        almostNote.setVisibility(almostTab ? View.VISIBLE : View.GONE);
        footnote.setAlpha(almostTab ? 0f : 1f);

        boolean empty = rows.isEmpty();
        emptyState.setVisibility(empty ? View.VISIBLE : View.GONE);
        recycler.setVisibility(empty ? View.GONE : View.VISIBLE);
        if (empty) {
            if (almostTab) {
                emptyTitle.setText(R.string.almost_empty_title);
                emptyMessage.setText(R.string.almost_empty_message);
                emptyAddButton.setVisibility(View.GONE);
            } else {
                // Required feedback when zero recipes match the pantry (no blank screen).
                emptyTitle.setText(R.string.no_match_title);
                emptyMessage.setText(R.string.no_match_message);
                emptyAddButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRecipeClicked(RecipeAdapter.Row row) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, row.recipe.recipe.id); // pass the selected recipe
        startActivity(intent);
    }
}
