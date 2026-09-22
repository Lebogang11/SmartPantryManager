package com.smartpantry.logic;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit handling for the strict-matching rule.
 *
 * Every unit belongs to a Dimension (mass, volume or count) and has a factor to that dimension's
 * base unit (g, ml, piece). Amounts in the same dimension are compared directly. Across dimensions
 * (e.g. "1 tsp salt" needed, "200 g salt" owned) a small density / piece-weight table is used;
 * if the table has no entry the units are reported as not comparable instead of guessing.
 */
public final class UnitConverter {

    public enum Dimension { MASS, VOLUME, COUNT }

    /** Units offered in the UI, in display order. */
    public static final String[] UNITS = {"g", "kg", "ml", "l", "tsp", "tbsp", "cup", "pcs", "can"};

    private static final Map<String, Dimension> DIM = new HashMap<>();
    private static final Map<String, Double> FACTOR = new HashMap<>();
    private static final Map<String, Double> DENSITY_G_PER_ML = new HashMap<>();
    private static final Map<String, Double> PIECE_G = new HashMap<>();

    private static void unit(String u, Dimension d, double f) { DIM.put(u, d); FACTOR.put(u, f); }

    static {
        unit("g", Dimension.MASS, 1);      unit("kg", Dimension.MASS, 1000);
        unit("ml", Dimension.VOLUME, 1);   unit("l", Dimension.VOLUME, 1000);
        unit("tsp", Dimension.VOLUME, 5);  unit("tbsp", Dimension.VOLUME, 15); unit("cup", Dimension.VOLUME, 240);
        unit("pcs", Dimension.COUNT, 1);   unit("can", Dimension.COUNT, 1);

        Object[][] density = {{"salt", 1.2}, {"sugar", 0.85}, {"flour", 0.53}, {"butter", 0.96}, {"honey", 1.4},
                {"oat", 0.36}, {"rice", 0.85}, {"cumin", 0.45}, {"curry powder", 0.5}, {"chilli flake", 0.35},
                {"olive oil", 0.91}, {"vegetable oil", 0.92}, {"soy sauce", 1.2}, {"milk", 1.03}, {"cream", 1.0},
                {"yoghurt", 1.03}, {"mayonnaise", 0.95}, {"water", 1.0}, {"lentil", 0.85},
                {"vegetable stock", 1.0}, {"coconut milk", 1.0}};
        for (Object[] d : density) DENSITY_G_PER_ML.put((String) d[0], (Double) d[1]);

        Object[][] pieces = {{"egg", 55.0}, {"tomato", 120.0}, {"potato", 170.0}, {"onion", 150.0}, {"garlic", 5.0},
                {"carrot", 80.0}, {"bell pepper", 160.0}, {"banana", 120.0}, {"avocado", 170.0}, {"lime", 65.0},
                {"lemon", 90.0}, {"bread", 30.0}, {"apple", 180.0}, {"spring onion", 15.0}};
        for (Object[] p : pieces) PIECE_G.put((String) p[0], (Double) p[1]);
    }

    private UnitConverter() { }

    public static boolean isKnown(String unit) { return unit != null && DIM.containsKey(unit); }

    public static Dimension dimensionOf(String unit) { return DIM.get(unit); }

    /** Multiplier from this unit to its dimension's base unit (kg -> 1000, tbsp -> 15, pcs -> 1). */
    public static double factorOf(String unit) { return FACTOR.get(unit); }

    /** Converts a quantity in the given unit to the base unit of its dimension. */
    public static double toBase(double quantity, String unit) { return quantity * FACTOR.get(unit); }

    /**
     * How many base units of {@code to} equal one base unit of {@code from} for this ingredient,
     * or null when the conversion is not known.
     */
    public static Double crossFactor(Dimension from, Dimension to, String canonicalName) {
        if (from == to) return 1.0;
        Double a = gramsPerBase(from, canonicalName), b = gramsPerBase(to, canonicalName);
        return (a == null || b == null) ? null : a / b;
    }

    private static Double gramsPerBase(Dimension d, String name) {
        switch (d) {
            case MASS: return 1.0;
            case VOLUME: return DENSITY_G_PER_ML.get(name);
            default: return PIECE_G.get(name);
        }
    }
}
