package com.smartpantry.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.smartpantry.R;
import com.smartpantry.data.AppDatabase;
import com.smartpantry.data.DatabaseSeeder;
import com.smartpantry.logic.UnitConverter;
import com.smartpantry.util.SettingsManager;

/** Settings: expiring-soon alerts, alert window, ignore-expired rule, default unit, and demo-data tools. */
public class SettingsActivity extends BaseActivity {

    private static final int[] ALERT_DAY_CHOICES = {1, 2, 3, 5, 7};

    private SettingsManager settings;
    private AppDatabase db;
    private SwitchMaterial alertsSwitch, ignoreExpiredSwitch;
    private Spinner daysSpinner, unitSpinner;
    private boolean binding; // true while we set controls from saved values (so listeners don't re-save)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSupportActionBar(findViewById(R.id.toolbar));

        settings = new SettingsManager(this);
        db = AppDatabase.get(this);

        alertsSwitch = findViewById(R.id.switchAlerts);
        ignoreExpiredSwitch = findViewById(R.id.switchIgnoreExpired);
        daysSpinner = findViewById(R.id.spinnerDays);
        unitSpinner = findViewById(R.id.spinnerDefaultUnit);

        String[] dayLabels = new String[ALERT_DAY_CHOICES.length];
        for (int i = 0; i < dayLabels.length; i++) dayLabels[i] = ALERT_DAY_CHOICES[i] + (ALERT_DAY_CHOICES[i] == 1 ? " day" : " days");
        daysSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dayLabels));
        unitSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, UnitConverter.UNITS));

        alertsSwitch.setOnCheckedChangeListener((b, checked) -> { if (!binding) settings.setAlertsEnabled(checked); });
        ignoreExpiredSwitch.setOnCheckedChangeListener((b, checked) -> { if (!binding) settings.setIgnoreExpired(checked); });
        daysSpinner.setOnItemSelectedListener(new SimpleSelect(pos -> { if (!binding) settings.setAlertDays(ALERT_DAY_CHOICES[pos]); }));
        unitSpinner.setOnItemSelectedListener(new SimpleSelect(pos -> { if (!binding) settings.setDefaultUnit(UnitConverter.UNITS[pos]); }));

        findViewById(R.id.buttonLoadDemo).setOnClickListener(v -> confirm("Load demo pantry?",
                "This replaces your current pantry with 18 sample ingredients.", "Load demo",
                () -> AppDatabase.io().execute(() -> {
                    DatabaseSeeder.loadDemoPantry(db);
                    ui(() -> toast("Demo pantry loaded"));
                })));
        findViewById(R.id.buttonClearPantry).setOnClickListener(v -> confirm("Clear the pantry?",
                "Every ingredient will be removed. Recipes are kept.", "Clear pantry",
                () -> AppDatabase.io().execute(() -> {
                    DatabaseSeeder.clearPantry(db);
                    ui(() -> toast("Pantry cleared"));
                })));
        findViewById(R.id.buttonReset).setOnClickListener(v -> confirm("Reset the app?",
                "This restores default settings and the demo pantry.", "Reset",
                () -> AppDatabase.io().execute(() -> {
                    DatabaseSeeder.loadDemoPantry(db);
                    settings.resetToDefaults();
                    ui(() -> { bindValues(); toast("App reset"); });
                })));

        setupBottomNav(R.id.nav_settings);
        bindValues();
    }

    private void bindValues() {
        binding = true;
        alertsSwitch.setChecked(settings.isAlertsEnabled());
        ignoreExpiredSwitch.setChecked(settings.isIgnoreExpired());
        for (int i = 0; i < ALERT_DAY_CHOICES.length; i++) if (ALERT_DAY_CHOICES[i] == settings.getAlertDays()) daysSpinner.setSelection(i);
        for (int i = 0; i < UnitConverter.UNITS.length; i++) if (UnitConverter.UNITS[i].equals(settings.getDefaultUnit())) unitSpinner.setSelection(i);
        binding = false;
    }

    private void confirm(String title, String message, String action, Runnable onConfirm) {
        new MaterialAlertDialogBuilder(this).setTitle(title).setMessage(message)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(action, (d, w) -> onConfirm.run()).show();
    }

    private void toast(String text) { Toast.makeText(this, text, Toast.LENGTH_SHORT).show(); }

    /** Spinner listener that only reports the selected position. */
    private static class SimpleSelect implements AdapterView.OnItemSelectedListener {
        interface OnPick { void pick(int position); }
        private final OnPick onPick;
        SimpleSelect(OnPick onPick) { this.onPick = onPick; }
        @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { onPick.pick(position); }
        @Override public void onNothingSelected(AdapterView<?> parent) { }
    }
}
