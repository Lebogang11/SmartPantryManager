package com.smartpantry.util;

import java.util.Locale;

/** Small display helpers shared by the adapters and activities. */
public final class Formatters {
    private Formatters() { }

    /** 3.0 -> "3", 0.5 -> "0.5", 2.345 -> "2.35". */
    public static String number(double value) {
        double rounded = Math.round(value * 100.0) / 100.0;
        if (rounded == Math.rint(rounded)) return String.valueOf((long) rounded);
        return String.valueOf(rounded);
    }

    public static String quantity(double value, String unit) { return number(value) + " " + unit; }

    /** "tomatoes" -> "Tomatoes". */
    public static String capitalise(String s) {
        if (s == null || s.isEmpty()) return "";
        return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1);
    }

    /** Human-readable expiry text for a days-until-expiry value (null = no expiry date). */
    public static String expiryLabel(Integer days) {
        if (days == null) return "No expiry date";
        if (days < -1) return "Expired " + (-days) + " days ago";
        if (days == -1) return "Expired yesterday";
        if (days == 0) return "Expires today";
        if (days == 1) return "Expires tomorrow";
        return "Expires in " + days + " days";
    }

    public static String plural(int n, String word) { return n + " " + word + (n == 1 ? "" : "s"); }
}
