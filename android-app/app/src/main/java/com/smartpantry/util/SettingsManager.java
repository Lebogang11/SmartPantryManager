package com.smartpantry.util;

import android.content.Context;
import android.content.SharedPreferences;

/** Reads and writes the user's preferences (SharedPreferences, so they survive app restarts). */
public class SettingsManager {

    private static final String FILE = "smartpantry_prefs";
    private static final String KEY_ALERTS = "alerts_enabled";
    private static final String KEY_DAYS = "alert_days";
    private static final String KEY_IGNORE_EXPIRED = "ignore_expired";
    private static final String KEY_DEFAULT_UNIT = "default_unit";

    private final SharedPreferences prefs;

    public SettingsManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(FILE, Context.MODE_PRIVATE);
    }

    public boolean isAlertsEnabled() { return prefs.getBoolean(KEY_ALERTS, true); }
    public void setAlertsEnabled(boolean v) { prefs.edit().putBoolean(KEY_ALERTS, v).apply(); }

    /** How many days ahead an ingredient counts as "expiring soon". */
    public int getAlertDays() { return prefs.getInt(KEY_DAYS, 3); }
    public void setAlertDays(int v) { prefs.edit().putInt(KEY_DAYS, v).apply(); }

    /** When true, expired pantry items do not count towards suggested recipes. */
    public boolean isIgnoreExpired() { return prefs.getBoolean(KEY_IGNORE_EXPIRED, true); }
    public void setIgnoreExpired(boolean v) { prefs.edit().putBoolean(KEY_IGNORE_EXPIRED, v).apply(); }

    public String getDefaultUnit() { return prefs.getString(KEY_DEFAULT_UNIT, "pcs"); }
    public void setDefaultUnit(String v) { prefs.edit().putString(KEY_DEFAULT_UNIT, v).apply(); }

    public void resetToDefaults() { prefs.edit().clear().apply(); }
}
