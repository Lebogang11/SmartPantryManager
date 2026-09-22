package com.smartpantry.logic;

import com.smartpantry.logic.UnitConverter.Dimension;
import com.smartpantry.util.Formatters;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * THE STRICT-MATCHING RULE (assignment section 2.3).
 *
 * A recipe is "ready" only if EVERY ingredient it requires is in the pantry in at least the
 * required quantity. One missing or insufficient ingredient means the recipe is not suggested.
 *
 * Matching is tolerant of everyday messiness: names are normalised (plural/singular, adjectives,
 * synonyms), quantities are converted between units (g/kg, ml/l/tsp/tbsp/cup, pcs) and several
 * pantry entries for the same ingredient are added together.
 */
public final class RecipeMatcher {

    /** Days until expiry, or null when there is no expiry date. Negative means already expired. */
    public static Integer daysUntilExpiry(String isoDate, LocalDate today) {
        if (isoDate == null || isoDate.isEmpty()) return null;
        return (int) ChronoUnit.DAYS.between(today, LocalDate.parse(isoDate));
    }

    public static boolean isExpired(Stock s, LocalDate today) {
        Integer d = daysUntilExpiry(s.getExpiryDate(), today);
        return d != null && d < 0;
    }

    public static boolean isExpiringSoon(Stock s, LocalDate today, int soonDays) {
        Integer d = daysUntilExpiry(s.getExpiryDate(), today);
        return d != null && d >= 0 && d <= soonDays;
    }

    private static boolean usable(Stock s, boolean excludeExpired, LocalDate today) {
        return UnitConverter.isKnown(s.getUnit()) && !(excludeExpired && isExpired(s, today));
    }

    /** Total pantry amount for one canonical ingredient, kept separately per dimension. */
    private static final class Group {
        final Map<Dimension, Double> totals = new EnumMap<>(Dimension.class);
        final List<Stock> items = new ArrayList<>();
        double get(Dimension d) { Double v = totals.get(d); return v == null ? 0 : v; }
    }

    /** Checks one recipe against the pantry and explains the decision line by line. */
    public static MatchResult evaluate(long recipeId, List<? extends Requirement> required,
                                       List<? extends Stock> pantry, boolean excludeExpired,
                                       LocalDate today, int soonDays) {
        // 1. Group usable pantry entries by canonical ingredient name.
        Map<String, Group> groups = new HashMap<>();
        for (Stock s : pantry) {
            if (!usable(s, excludeExpired, today)) continue;
            String key = IngredientNormalizer.normalize(s.getName());
            Group g = groups.get(key);
            if (g == null) { g = new Group(); groups.put(key, g); }
            Dimension d = UnitConverter.dimensionOf(s.getUnit());
            g.totals.put(d, g.get(d) + UnitConverter.toBase(s.getQuantity(), s.getUnit()));
            g.items.add(s);
        }

        // 2. Check every recipe ingredient. No shortcuts: all of them are inspected.
        MatchResult result = new MatchResult(recipeId);
        Set<String> expiring = new HashSet<>();
        for (Requirement r : required) {
            String key = IngredientNormalizer.normalize(r.getName());
            Dimension needDim = UnitConverter.dimensionOf(r.getUnit());
            double needBase = UnitConverter.toBase(r.getQuantity(), r.getUnit());
            String need = Formatters.quantity(r.getQuantity(), r.getUnit());
            Group g = groups.get(key);

            if (g == null) {
                result.lines.add(new MatchResult.Line(r.getName(), need, "", MatchResult.Status.MISSING));
                continue;
            }
            double total = g.get(needDim);
            boolean unitMismatch = false;
            for (Dimension other : Dimension.values()) {
                if (other == needDim || g.get(other) == 0) continue;
                Double f = UnitConverter.crossFactor(other, needDim, key);
                if (f == null) unitMismatch = true; else total += g.get(other) * f;
            }
            StringBuilder have = new StringBuilder();
            for (Stock s : g.items) {
                if (have.length() > 0) have.append(" + ");
                have.append(Formatters.quantity(s.getQuantity(), s.getUnit()));
            }
            MatchResult.Status status;
            if (total + 1e-9 >= needBase) status = MatchResult.Status.OK;
            else if (total == 0 && unitMismatch) status = MatchResult.Status.UNIT_MISMATCH;
            else status = MatchResult.Status.SHORT;
            result.lines.add(new MatchResult.Line(r.getName(), need, have.toString(), status));

            if (status == MatchResult.Status.OK) {
                for (Stock s : g.items) if (isExpiringSoon(s, today, soonDays)) expiring.add(s.getName());
            }
        }
        result.expiringUsed.addAll(expiring);
        return result;
    }

    /**
     * Deducts a cooked recipe from the pantry, using the earliest-expiring stock first.
     * Returns pantryItemId -> new quantity for every entry that changed (quantity 0 = delete it).
     */
    public static Map<Long, Double> planCooking(List<? extends Requirement> required,
                                                List<? extends Stock> pantry, boolean excludeExpired,
                                                LocalDate today) {
        Map<Long, Double> next = new LinkedHashMap<>();
        for (Stock s : pantry) next.put(s.getId(), s.getQuantity());

        for (Requirement r : required) {
            String key = IngredientNormalizer.normalize(r.getName());
            Dimension needDim = UnitConverter.dimensionOf(r.getUnit());
            double remaining = UnitConverter.toBase(r.getQuantity(), r.getUnit());

            List<Stock> candidates = new ArrayList<>();
            for (Stock s : pantry) {
                if (usable(s, excludeExpired, today) && IngredientNormalizer.normalize(s.getName()).equals(key)) {
                    candidates.add(s);
                }
            }
            candidates.sort(Comparator
                    .comparingLong((Stock s) -> s.getExpiryDate() == null ? Long.MAX_VALUE : LocalDate.parse(s.getExpiryDate()).toEpochDay())
                    .thenComparingLong(Stock::getId));

            for (Stock s : candidates) {
                if (remaining <= 1e-9) break;
                Dimension d = UnitConverter.dimensionOf(s.getUnit());
                Double f = UnitConverter.crossFactor(d, needDim, key);
                if (f == null) continue;
                double unitFactor = UnitConverter.factorOf(s.getUnit());
                double itemBase = next.get(s.getId()) * unitFactor;
                double take = Math.min(remaining, itemBase * f);
                next.put(s.getId(), Math.round(((itemBase - take / f) / unitFactor) * 1000.0) / 1000.0);
                remaining -= take;
            }
        }

        Map<Long, Double> changes = new LinkedHashMap<>();
        for (Stock s : pantry) {
            double n = next.get(s.getId());
            if (n != s.getQuantity()) changes.put(s.getId(), n < 0.005 ? 0.0 : n);
        }
        return changes;
    }
}
