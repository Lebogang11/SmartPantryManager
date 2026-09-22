package com.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.smartpantry.R;
import com.smartpantry.adapter.RecipeIngredientAdapter;
import com.smartpantry.data.AppDatabase;
import com.smartpantry.data.PantryItem;
import com.smartpantry.data.RecipeIngredient;
import com.smartpantry.data.RecipeWithIngredients;
import com.smartpantry.logic.IngredientNormalizer;
import com.smartpantry.logic.MatchResult;
import com.smartpantry.logic.RecipeMatcher;
import com.smartpantry.util.Formatters;
import com.smartpantry.util.SettingsManager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Recipe detail: full ingredient list (tick / cross against the pantry), numbered method,
 * a "How this was checked" explanation of the strict rule, and a "Mark as cooked" action that
 * deducts the ingredients from the pantry (earliest-expiring stock first).
 * The recipe id arrives through the launching Intent (EXTRA_RECIPE_ID).
 */
public class RecipeDetailActivity extends BaseActivity implements RecipeIngredientAdapter.Listener {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    private AppDatabase db;
    private SettingsManager settings;
    private RecipeIngredientAdapter adapter;

    private RecipeWithIngredients recipe;
    private List<PantryItem> pantry;
    private MatchResult result;

    private TextView emoji, title, meta, status, steps, trace, hint;
    private Button cookButton, traceButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = AppDatabase.get(this);
        settings = new SettingsManager(this);

        emoji = findViewById(R.id.textEmoji);
        title = findViewById(R.id.textTitle);
        meta = findViewById(R.id.textMeta);
        status = findViewById(R.id.textStatus);
        steps = findViewById(R.id.textSteps);
        trace = findViewById(R.id.textTrace);
        hint = findViewById(R.id.textHint);
        cookButton = findViewById(R.id.buttonCook);
        traceButton = findViewById(R.id.buttonTrace);

        RecyclerView recycler = findViewById(R.id.recyclerIngredients);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setNestedScrollingEnabled(false);
        adapter = new RecipeIngredientAdapter(this);
        recycler.setAdapter(adapter);

        traceButton.setOnClickListener(v -> {
            boolean show = trace.getVisibility() != View.VISIBLE;
            trace.setVisibility(show ? View.VISIBLE : View.GONE);
            traceButton.setText(show ? R.string.action_hide_trace : R.string.action_show_trace);
        });
        cookButton.setOnClickListener(v -> confirmCook());
    }

    /** Reload on every resume: after adding a missing ingredient the status updates when the user returns. */
    @Override
    protected void onResume() {
        super.onResume();
        final long id = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);
        AppDatabase.io().execute(() -> {
            final RecipeWithIngredients r = db.recipeDao().getById(id);
            final List<PantryItem> items = db.pantryDao().getAll();
            ui(() -> {
                if (r == null) { Toast.makeText(this, "Recipe not found.", Toast.LENGTH_SHORT).show(); finish(); return; }
                recipe = r;
                pantry = items;
                bind();
            });
        });
    }

    private void bind() {
        result = RecipeMatcher.evaluate(recipe.recipe.id, recipe.ingredients, pantry,
                settings.isIgnoreExpired(), LocalDate.now(), settings.getAlertDays());

        emoji.setText(recipe.recipe.emoji);
        title.setText(recipe.recipe.name);
        meta.setText(recipe.recipe.minutes + " min  •  serves " + recipe.recipe.servings);

        int missing = result.missingCount();
        if (result.isReady()) {
            status.setText("You have everything for this recipe.");
            status.setBackgroundResource(R.drawable.bg_status_ok);
        } else if (missing == 1) {
            status.setText("Missing 1 ingredient, so it isn’t suggested yet.");
            status.setBackgroundResource(R.drawable.bg_status_almost);
        } else {
            status.setText("Missing " + missing + " ingredients, so it isn’t suggested.");
            status.setBackgroundResource(R.drawable.bg_status_no);
        }

        adapter.submit(result.lines);

        StringBuilder sb = new StringBuilder();
        int n = 1;
        for (String step : recipe.recipe.stepList()) sb.append(n++).append(". ").append(step).append("\n\n");
        steps.setText(sb.toString().trim());

        StringBuilder t = new StringBuilder("A recipe is suggested only if every ingredient is covered.\n");
        for (MatchResult.Line l : result.lines) {
            t.append("\n• ").append(l.name).append(" → ").append(IngredientNormalizer.normalize(l.name))
                    .append("\n  need ").append(l.need)
                    .append(l.have.isEmpty() ? ", not in pantry" : ", pantry holds " + l.have)
                    .append(" → ").append(l.isOk() ? "covered" : "NOT covered");
        }
        trace.setText(t);

        cookButton.setVisibility(result.isReady() ? View.VISIBLE : View.GONE);
        hint.setVisibility(result.isReady() ? View.GONE : View.VISIBLE);
        hint.setText("Add the missing " + (missing == 1 ? "ingredient" : "ingredients") + " to your pantry to unlock cooking.");
    }

    @Override
    public void onAddMissing(int position) {
        RecipeIngredient ing = recipe.ingredients.get(position);
        Intent intent = new Intent(this, AddEditIngredientActivity.class);
        intent.putExtra(AddEditIngredientActivity.EXTRA_PREFILL_NAME, Formatters.capitalise(ing.name));
        intent.putExtra(AddEditIngredientActivity.EXTRA_PREFILL_QTY, ing.quantity);
        intent.putExtra(AddEditIngredientActivity.EXTRA_PREFILL_UNIT, ing.unit);
        startActivity(intent);
    }

    /** Shows exactly what will be deducted, then updates the pantry in one database transaction. */
    private void confirmCook() {
        final Map<Long, Double> plan = RecipeMatcher.planCooking(recipe.ingredients, pantry,
                settings.isIgnoreExpired(), LocalDate.now());
        final Map<Long, PantryItem> byId = new HashMap<>();
        for (PantryItem p : pantry) byId.put(p.id, p);

        StringBuilder msg = new StringBuilder("Your pantry will change like this:\n");
        for (Map.Entry<Long, Double> e : plan.entrySet()) {
            PantryItem p = byId.get(e.getKey());
            msg.append("\n• ").append(Formatters.capitalise(p.name)).append(": ")
                    .append(e.getValue() <= 0
                            ? Formatters.quantity(p.quantity, p.unit) + " → used up"
                            : Formatters.number(p.quantity) + " → " + Formatters.quantity(e.getValue(), p.unit));
        }
        new MaterialAlertDialogBuilder(this)
                .setTitle("Cook " + recipe.recipe.name + "?")
                .setMessage(msg)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton("Mark as cooked", (d, w) -> AppDatabase.io().execute(() -> {
                    db.runInTransaction(() -> {
                        for (Map.Entry<Long, Double> e : plan.entrySet()) {
                            PantryItem p = byId.get(e.getKey());
                            if (e.getValue() <= 0) db.pantryDao().delete(p);
                            else { p.quantity = e.getValue(); db.pantryDao().update(p); }
                        }
                    });
                    ui(() -> {
                        Toast.makeText(this, "Enjoy your " + recipe.recipe.name.toLowerCase() + "! Pantry updated.", Toast.LENGTH_LONG).show();
                        finish(); // back to the Suggested Recipes screen, which re-runs the matching in onResume()
                    });
                }))
                .show();
    }
}
