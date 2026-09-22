package com.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.smartpantry.R;
import com.smartpantry.adapter.PantryAdapter;
import com.smartpantry.data.AppDatabase;
import com.smartpantry.data.DatabaseSeeder;
import com.smartpantry.data.PantryItem;
import com.smartpantry.logic.IngredientNormalizer;
import com.smartpantry.logic.RecipeMatcher;
import com.smartpantry.util.Formatters;
import com.smartpantry.util.SettingsManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Launcher screen: the pantry list.
 */
public class PantryListActivity extends BaseActivity implements PantryAdapter.Listener {

    private static final int FILTER_ALL = 0, FILTER_SOON = 1, FILTER_EXPIRED = 2;

    private AppDatabase db;
    private SettingsManager settings;
    private PantryAdapter adapter;

    private List<PantryItem> allItems = new ArrayList<>();
    private String query = "";
    private int filter = FILTER_ALL;
    private int sortMode = 0; // 0 expiry, 1 name, 2 recently added

    private TextView alertBanner, countText, emptyTitle, emptyMessage;
    private View emptyState;
    private Button emptyAddButton, emptyDemoButton;
    private RecyclerView recycler;
    private Chip chipAll, chipSoon, chipExpired;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);
        setSupportActionBar(findViewById(R.id.toolbar));

        db = AppDatabase.get(this);
        settings = new SettingsManager(this);

        alertBanner = findViewById(R.id.alertBanner);
        countText = findViewById(R.id.textCount);
        emptyState = findViewById(R.id.emptyState);
        emptyTitle = findViewById(R.id.emptyTitle);
        emptyMessage = findViewById(R.id.emptyMessage);
        emptyAddButton = findViewById(R.id.buttonEmptyAdd);
        emptyDemoButton = findViewById(R.id.buttonEmptyDemo);
        chipAll = findViewById(R.id.chipAll);
        chipSoon = findViewById(R.id.chipSoon);
        chipExpired = findViewById(R.id.chipExpired);

        recycler = findViewById(R.id.recyclerPantry);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(this);
        recycler.setAdapter(adapter);

        setupSearch();
        setupFilters();
        setupSort();
        setupBottomNav(R.id.nav_pantry);

        ExtendedFloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> openAddScreen());
        emptyAddButton.setOnClickListener(v -> openAddScreen());
        emptyDemoButton.setOnClickListener(v -> AppDatabase.io().execute(() -> {
            DatabaseSeeder.loadDemoPantry(db);
            loadPantry();
        }));
        alertBanner.setOnClickListener(v -> chipSoon.setChecked(true));

        // First run: write the recipe collection to the database (runs before the first load below).
        AppDatabase.io().execute(() -> DatabaseSeeder.ensureRecipesSeeded(db));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantry();
    }

    private void setupSearch() {
        EditText search = findViewById(R.id.editSearch);
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void afterTextChanged(Editable s) {
                query = s.toString();
                applyFilters();
            }
        });
    }

    private void setupFilters() {
        ChipGroup group = findViewById(R.id.chipGroup);
        group.setOnCheckedStateChangeListener((g, ids) -> {
            if (ids.isEmpty()) return;
            int id = ids.get(0);
            filter = id == R.id.chipSoon ? FILTER_SOON : id == R.id.chipExpired ? FILTER_EXPIRED : FILTER_ALL;
            applyFilters();
        });
    }

    private void setupSort() {
        Spinner sort = findViewById(R.id.spinnerSort);
        sort.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.sort_options)));
        sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                sortMode = pos;
                applyFilters();
            }
            @Override public void onNothingSelected(AdapterView<?> p) { }
        });
    }

    /** Reads the pantry from SQLite on the background thread, then updates the screen. */
    private void loadPantry() {
        AppDatabase.io().execute(() -> {
            final List<PantryItem> items = db.pantryDao().getAll();
            ui(() -> {
                allItems = items;
                applyFilters();
            });
        });
    }

    private void applyFilters() {
        final LocalDate today = LocalDate.now();
        final int soonDays = settings.getAlertDays();
        final String q = query.trim().toLowerCase(Locale.ROOT);
        final String qKey = IngredientNormalizer.normalize(query);

        int soonCount = 0, expiredCount = 0;
        List<PantryItem> shown = new ArrayList<>();
        for (PantryItem item : allItems) {
            boolean expired = RecipeMatcher.isExpired(item, today);
            boolean soon = RecipeMatcher.isExpiringSoon(item, today, soonDays);
            if (soon) soonCount++;
            if (expired) expiredCount++;

            if (!q.isEmpty()) {
                boolean matches = item.name.toLowerCase(Locale.ROOT).contains(q)
                        || (!qKey.isEmpty() && IngredientNormalizer.normalize(item.name).contains(qKey));
                if (!matches) continue;
            }
            if (filter == FILTER_SOON && !soon) continue;
            if (filter == FILTER_EXPIRED && !expired) continue;
            shown.add(item);
        }

        Comparator<PantryItem> byName = (a, b) -> a.name.compareToIgnoreCase(b.name);
        Comparator<PantryItem> byExpiry = Comparator
                .comparing((PantryItem p) -> p.expiryDate == null ? "9999-99-99" : p.expiryDate)
                .thenComparing(byName);
        if (sortMode == 1) shown.sort(byName);
        else if (sortMode == 2) shown.sort((a, b) -> Long.compare(b.addedAt, a.addedAt));
        else shown.sort(byExpiry);

        adapter.submit(shown, today, soonDays);

        chipAll.setText("All (" + allItems.size() + ")");
        chipSoon.setText("Expiring soon (" + soonCount + ")");
        chipExpired.setText("Expired (" + expiredCount + ")");
        countText.setText(Formatters.plural(shown.size(), "result"));
        getSupportActionBar().setSubtitle(Formatters.plural(allItems.size(), "ingredient") + " at home");

        updateBanner(soonCount, expiredCount, soonDays);
        updateEmptyState(shown.isEmpty());
    }

    /** Expiring-soon alert (controlled by the Settings screen). */
    private void updateBanner(int soon, int expired, int soonDays) {
        if (!settings.isAlertsEnabled() || (soon + expired) == 0) {
            alertBanner.setVisibility(View.GONE);
            return;
        }
        StringBuilder text = new StringBuilder();
        if (soon > 0) text.append(Formatters.plural(soon, "item")).append(soon == 1 ? " expires" : " expire")
                .append(" within ").append(Formatters.plural(soonDays, "day"));
        if (soon > 0 && expired > 0) text.append(". ");
        if (expired > 0) text.append(Formatters.plural(expired, "item")).append(expired == 1 ? " has" : " have").append(" expired");
        text.append("  (tap to show)");
        alertBanner.setText(text);
        alertBanner.setVisibility(View.VISIBLE);
    }

    private void updateEmptyState(boolean noRows) {
        emptyState.setVisibility(noRows ? View.VISIBLE : View.GONE);
        recycler.setVisibility(noRows ? View.GONE : View.VISIBLE);
        if (!noRows) return;
        boolean pantryEmpty = allItems.isEmpty();
        emptyTitle.setText(pantryEmpty ? R.string.empty_pantry_title : R.string.no_results_title);
        emptyMessage.setText(pantryEmpty ? R.string.empty_pantry_message : R.string.no_results_message);
        emptyAddButton.setVisibility(pantryEmpty ? View.VISIBLE : View.GONE);
        emptyDemoButton.setVisibility(pantryEmpty ? View.VISIBLE : View.GONE);
    }

    private void openAddScreen() {
        startActivity(new Intent(this, AddEditIngredientActivity.class));
    }

    @Override
    public void onEdit(PantryItem item) {
        Intent intent = new Intent(this, AddEditIngredientActivity.class);
        intent.putExtra(AddEditIngredientActivity.EXTRA_ITEM_ID, item.id); // data passed to the next screen
        startActivity(intent);
    }

    @Override
    public void onDelete(PantryItem item) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Delete " + Formatters.capitalise(item.name) + "?")
                .setMessage("This removes " + Formatters.quantity(item.quantity, item.unit) + " from your pantry.")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton("Delete", (d, w) -> AppDatabase.io().execute(() -> {
                    db.pantryDao().delete(item);
                    loadPantry();
                    ui(() -> Snackbar.make(recycler, Formatters.capitalise(item.name) + " deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", v -> AppDatabase.io().execute(() -> {
                                db.pantryDao().insert(item); // same id, so the row comes back unchanged
                                loadPantry();
                            })).show());
                }))
                .show();
    }
}
