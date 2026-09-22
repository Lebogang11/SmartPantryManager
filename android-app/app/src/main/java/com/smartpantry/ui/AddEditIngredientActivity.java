package com.smartpantry.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.smartpantry.R;
import com.smartpantry.data.AppDatabase;
import com.smartpantry.data.PantryItem;
import com.smartpantry.logic.IngredientNormalizer;
import com.smartpantry.logic.RecipeMatcher;
import com.smartpantry.logic.UnitConverter;
import com.smartpantry.util.Formatters;
import com.smartpantry.util.SettingsManager;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Add / Edit ingredient screen (one Activity, two modes). If the launching Intent carries
 * EXTRA_ITEM_ID the screen loads that row and saves an UPDATE; otherwise it saves an INSERT.
 * The recipe detail screen can also open it with pre-filled values (EXTRA_PREFILL_*).
 * Every field is validated before anything is written to the database.
 */
public class AddEditIngredientActivity extends BaseActivity {

    public static final String EXTRA_ITEM_ID = "item_id";
    public static final String EXTRA_PREFILL_NAME = "prefill_name";
    public static final String EXTRA_PREFILL_QTY = "prefill_qty";
    public static final String EXTRA_PREFILL_UNIT = "prefill_unit";

    private static final Pattern NAME_PATTERN = Pattern.compile("^\\p{L}[\\p{L}\\s'’().,-]*$");

    private AppDatabase db;
    private long itemId = -1;
    private PantryItem editing;
    private String expiry; // ISO date or null

    private TextInputLayout nameLayout, quantityLayout, expiryLayout;
    private TextInputEditText nameEdit, quantityEdit, expiryEdit;
    private Spinner unitSpinner;
    private TextView nameHint, expiryWarning;
    private Button saveButton, deleteButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = AppDatabase.get(this);
        itemId = getIntent().getLongExtra(EXTRA_ITEM_ID, -1);

        nameLayout = findViewById(R.id.layoutName);
        quantityLayout = findViewById(R.id.layoutQuantity);
        expiryLayout = findViewById(R.id.layoutExpiry);
        nameEdit = findViewById(R.id.editName);
        quantityEdit = findViewById(R.id.editQuantity);
        expiryEdit = findViewById(R.id.editExpiry);
        unitSpinner = findViewById(R.id.spinnerUnit);
        nameHint = findViewById(R.id.textNameHint);
        expiryWarning = findViewById(R.id.textExpiryWarning);
        saveButton = findViewById(R.id.buttonSave);
        deleteButton = findViewById(R.id.buttonDelete);

        unitSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, UnitConverter.UNITS));
        selectUnit(new SettingsManager(this).getDefaultUnit());

        nameEdit.addTextChangedListener(new SimpleWatcher(this::onNameChanged));
        expiryEdit.setOnClickListener(v -> showDatePicker());
        findViewById(R.id.buttonClearDate).setOnClickListener(v -> setExpiry(null));
        saveButton.setOnClickListener(v -> save());
        findViewById(R.id.buttonCancel).setOnClickListener(v -> finish());
        deleteButton.setOnClickListener(v -> confirmDelete());

        if (itemId != -1) {
            getSupportActionBar().setTitle(R.string.title_edit_ingredient);
            saveButton.setText(R.string.action_save_changes);
            deleteButton.setVisibility(View.VISIBLE);
            AppDatabase.io().execute(() -> {
                final PantryItem item = db.pantryDao().getById(itemId);
                ui(() -> {
                    if (item == null) { Toast.makeText(this, "That ingredient no longer exists.", Toast.LENGTH_SHORT).show(); finish(); return; }
                    editing = item;
                    nameEdit.setText(item.name);
                    quantityEdit.setText(Formatters.number(item.quantity));
                    selectUnit(item.unit);
                    setExpiry(item.expiryDate);
                });
            });
        } else {
            getSupportActionBar().setTitle(R.string.title_add_ingredient);
            // Optional pre-fill, sent by the recipe detail screen's "Add" button.
            String prefillName = getIntent().getStringExtra(EXTRA_PREFILL_NAME);
            if (prefillName != null) {
                nameEdit.setText(prefillName);
                double qty = getIntent().getDoubleExtra(EXTRA_PREFILL_QTY, 0);
                if (qty > 0) quantityEdit.setText(Formatters.number(qty));
                String unit = getIntent().getStringExtra(EXTRA_PREFILL_UNIT);
                if (unit != null) selectUnit(unit);
            }
        }
        onNameChanged();
    }

    private void selectUnit(String unit) {
        for (int i = 0; i < UnitConverter.UNITS.length; i++) {
            if (UnitConverter.UNITS[i].equals(unit)) { unitSpinner.setSelection(i); return; }
        }
    }

    private void onNameChanged() {
        String key = IngredientNormalizer.normalize(nameEdit.getText() == null ? "" : nameEdit.getText().toString());
        nameHint.setText(key.isEmpty()
                ? getString(R.string.hint_name_default)
                : "Recognised as “" + key + "” when matching recipes.");
        nameLayout.setError(null);
    }

    private void setExpiry(@Nullable String iso) {
        expiry = iso;
        expiryEdit.setText(iso == null ? "" : iso);
        expiryLayout.setError(null);
        boolean past = iso != null && RecipeMatcher.daysUntilExpiry(iso, LocalDate.now()) < 0;
        expiryWarning.setVisibility(past ? View.VISIBLE : View.GONE);
    }

    private void showDatePicker() {
        LocalDate start = expiry != null ? LocalDate.parse(expiry) : LocalDate.now();
        new DatePickerDialog(this, (view, y, m, d) -> setExpiry(LocalDate.of(y, m + 1, d).toString()),
                start.getYear(), start.getMonthValue() - 1, start.getDayOfMonth()).show();
    }

    /** Validates all fields, shows an inline error on each bad one, and returns true only if everything is valid. */
    private boolean validate() {
        boolean ok = true;

        String name = nameEdit.getText() == null ? "" : nameEdit.getText().toString().trim();
        if (name.isEmpty()) { nameLayout.setError("Enter the ingredient name."); ok = false; }
        else if (name.length() < 2) { nameLayout.setError("Name must be at least 2 characters."); ok = false; }
        else if (name.length() > 40) { nameLayout.setError("Name must be 40 characters or fewer."); ok = false; }
        else if (!NAME_PATTERN.matcher(name).matches()) { nameLayout.setError("Use letters only, for example “Tomatoes”."); ok = false; }
        else nameLayout.setError(null);

        String qtyText = quantityEdit.getText() == null ? "" : quantityEdit.getText().toString().trim();
        if (qtyText.isEmpty()) { quantityLayout.setError("Enter a quantity."); ok = false; }
        else {
            try {
                double qty = Double.parseDouble(qtyText);
                if (Double.isNaN(qty) || Double.isInfinite(qty)) throw new NumberFormatException();
                if (qty <= 0) { quantityLayout.setError("Quantity must be greater than 0."); ok = false; }
                else if (qty > 100000) { quantityLayout.setError("Quantity is too large (maximum 100 000)."); ok = false; }
                else quantityLayout.setError(null);
            } catch (NumberFormatException e) {
                quantityLayout.setError("Quantity must be a number.");
                ok = false;
            }
        }

        if (expiry != null && RecipeMatcher.daysUntilExpiry(expiry, LocalDate.now()) > 3650) {
            expiryLayout.setError("Expiry date is more than 10 years away.");
            ok = false;
        } else expiryLayout.setError(null);

        return ok;
    }

    private void save() {
        if (!validate()) {
            Toast.makeText(this, "Fix the highlighted fields to continue.", Toast.LENGTH_SHORT).show();
            return;
        }
        final String name = nameEdit.getText().toString().trim().replaceAll("\\s+", " ");
        final double qty = Math.round(Double.parseDouble(quantityEdit.getText().toString().trim()) * 100.0) / 100.0;
        final String unit = (String) unitSpinner.getSelectedItem();
        final boolean isEdit = editing != null;

        AppDatabase.io().execute(() -> {
            if (isEdit) {
                editing.name = name;
                editing.quantity = qty;
                editing.unit = unit;
                editing.expiryDate = expiry;
                db.pantryDao().update(editing);          // UPDATE
            } else {
                db.pantryDao().insert(new PantryItem(name, qty, unit, expiry)); // CREATE
            }
            ui(() -> {
                Toast.makeText(this, Formatters.capitalise(name) + (isEdit ? " updated" : " added"), Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private void confirmDelete() {
        if (editing == null) return;
        new MaterialAlertDialogBuilder(this)
                .setTitle("Delete " + Formatters.capitalise(editing.name) + "?")
                .setMessage("This removes it from your pantry.")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton("Delete", (d, w) -> AppDatabase.io().execute(() -> {
                    db.pantryDao().delete(editing);       // DELETE
                    ui(() -> { Toast.makeText(this, "Ingredient deleted", Toast.LENGTH_SHORT).show(); finish(); });
                }))
                .show();
    }

    /** TextWatcher that only cares about "the text changed". */
    private static class SimpleWatcher implements TextWatcher {
        private final Runnable onChange;
        SimpleWatcher(Runnable onChange) { this.onChange = onChange; }
        @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
        @Override public void onTextChanged(CharSequence s, int a, int b, int c) { }
        @Override public void afterTextChanged(Editable s) { onChange.run(); }
    }
}
