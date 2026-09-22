package com.smartpantry.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.smartpantry.data.SeedData;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for the strict-matching rule (assignment section 2.3). They run on the JVM
 * (Android Studio: right-click the test class > Run) and need no emulator.
 */
public class RecipeMatcherTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 9, 20);

    // ---- tiny test doubles -------------------------------------------------------------
    private static class S implements Stock {
        final long id; final String name, unit, expiry; final double qty;
        S(long id, String name, double qty, String unit, String expiry) { this.id = id; this.name = name; this.qty = qty; this.unit = unit; this.expiry = expiry; }
        @Override public long getId() { return id; }
        @Override public String getName() { return name; }
        @Override public double getQuantity() { return qty; }
        @Override public String getUnit() { return unit; }
        @Override public String getExpiryDate() { return expiry; }
    }

    private static class R implements Requirement {
        final String name, unit; final double qty;
        R(String name, double qty, String unit) { this.name = name; this.qty = qty; this.unit = unit; }
        R(SeedData.Ing i) { this(i.name, i.quantity, i.unit); }
        @Override public String getName() { return name; }
        @Override public double getQuantity() { return qty; }
        @Override public String getUnit() { return unit; }
    }

    private static List<R> recipe(int index) {
        List<R> list = new ArrayList<>();
        for (SeedData.Ing i : SeedData.RECIPES.get(index).ingredients) list.add(new R(i));
        return list;
    }

    private static List<S> demoPantry() {
        List<S> pantry = new ArrayList<>();
        long id = 1;
        for (SeedData.DemoItem d : SeedData.DEMO_PANTRY) {
            String expiry = d.expiryOffsetDays == null ? null : TODAY.plusDays(d.expiryOffsetDays).toString();
            pantry.add(new S(id++, d.name, d.quantity, d.unit, expiry));
        }
        return pantry;
    }

    private static MatchResult check(List<? extends Requirement> req, List<? extends Stock> pantry) {
        return RecipeMatcher.evaluate(1, req, pantry, true, TODAY, 3);
    }

    // ---- the strict rule ---------------------------------------------------------------
    @Test public void fiveIngredientsWithFourInPantryIsNotSuggested() {
        List<R> req = Arrays.asList(new R("egg", 1, "pcs"), new R("milk", 1, "l"), new R("flour", 100, "g"),
                new R("butter", 10, "g"), new R("sugar", 20, "g"));
        List<S> pantry = Arrays.asList(new S(1, "egg", 2, "pcs", null), new S(2, "milk", 1, "l", null),
                new S(3, "flour", 500, "g", null), new S(4, "butter", 100, "g", null)); // sugar missing
        MatchResult m = check(req, pantry);
        assertFalse(m.isReady());
        assertTrue(m.isAlmostThere());
        assertEquals("sugar", m.firstProblem().name);
    }

    @Test public void exactQuantityPassesAndOneGramLessFails() {
        assertTrue(check(recipe(4), chickenPantry(300)).isReady());
        assertFalse(check(recipe(4), chickenPantry(299)).isReady());
    }

    private static List<S> chickenPantry(double grams) {
        return Arrays.asList(new S(1, "chicken breast", grams, "g", null), new S(2, "rice", 200, "g", null),
                new S(3, "onion", 1, "pcs", null), new S(4, "garlic", 2, "pcs", null), new S(5, "soy sauce", 2, "tbsp", null));
    }

    @Test public void demoPantryGivesExpectedReadyAndAlmostThereLists() {
        List<Integer> ready = new ArrayList<>(), almost = new ArrayList<>();
        List<S> pantry = demoPantry();
        for (int i = 0; i < SeedData.RECIPES.size(); i++) {
            MatchResult m = check(recipe(i), pantry);
            if (m.isReady()) ready.add(i + 1);
            if (m.isAlmostThere()) almost.add(i + 1);
        }
        assertEquals(Arrays.asList(1, 2, 5, 6, 8, 11, 13), ready);
        assertEquals(Arrays.asList(3, 4, 15), almost);
    }

    @Test public void removingOneIngredientRemovesRecipeFromSuggestions() {
        List<S> pantry = demoPantry();
        assertTrue(check(recipe(10), pantry).isReady());        // Cheese toastie
        pantry.removeIf(s -> s.name.equals("Cheddar cheese"));
        assertFalse(check(recipe(10), pantry).isReady());
    }

    @Test public void emptyPantryMatchesNothing() {
        for (int i = 0; i < SeedData.RECIPES.size(); i++) assertFalse(check(recipe(i), new ArrayList<S>()).isReady());
    }

    // ---- real-world messiness ----------------------------------------------------------
    @Test public void singularPluralAndSynonymsMatch() {
        List<S> pantry = Arrays.asList(new S(1, "Egg", 3, "pcs", null), new S(2, "Tomatoes", 1, "pcs", null),
                new S(3, "Cheddar", 30, "g", null), new S(4, "BUTTER ", 10, "g", null));
        assertTrue(check(recipe(0), pantry).isReady());
        assertEquals("tomato", IngredientNormalizer.normalize("Fresh Tomatoes (large)"));
        assertEquals("pasta", IngredientNormalizer.normalize("Spaghetti"));
    }

    @Test public void unitsAreConverted() {
        assertTrue(check(recipe(4), withSoy(30, "ml")).isReady());   // 2 tbsp = 30 ml
        assertFalse(check(recipe(4), withSoy(29, "ml")).isReady());
        assertTrue(check(recipe(4), withSoy(0.25, "cup")).isReady());
        assertTrue(check(recipe(4), withSoy(1, "l")).isReady());
    }

    private static List<S> withSoy(double qty, String unit) {
        return Arrays.asList(new S(1, "chicken breast", 0.3, "kg", null), new S(2, "rice", 200, "g", null),
                new S(3, "onions", 1, "pcs", null), new S(4, "garlic", 2, "pcs", null), new S(5, "soy sauce", qty, unit, null));
    }

    @Test public void separateBatchesOfTheSameIngredientAreAdded() {
        List<S> pantry = Arrays.asList(new S(1, "chicken", 150, "g", null), new S(6, "chicken breast", 150, "g", null),
                new S(2, "rice", 200, "g", null), new S(3, "onion", 1, "pcs", null), new S(4, "garlic", 2, "pcs", null),
                new S(5, "soy sauce", 2, "tbsp", null));
        assertTrue(check(recipe(4), pantry).isReady());
    }

    @Test public void unknownConversionIsReportedNotGuessed() {
        List<S> pantry = Arrays.asList(new S(1, "pasta", 200, "g", null), new S(2, "tomatoes", 500, "g", null),
                new S(3, "onion", 1, "cup", null), new S(4, "garlic", 2, "pcs", null), new S(5, "olive oil", 2, "tbsp", null),
                new S(6, "salt", 1, "tsp", null));
        MatchResult m = check(recipe(2), pantry);
        assertFalse(m.isReady());
        assertEquals(MatchResult.Status.UNIT_MISMATCH, m.firstProblem().status);
    }

    // ---- expiry ------------------------------------------------------------------------
    @Test public void expiredItemsCanBeExcluded() {
        List<S> pantry = demoPantry();
        pantry.replaceAll(s -> s.name.equals("Cheddar cheese") ? new S(s.id, s.name, s.qty, s.unit, TODAY.minusDays(1).toString()) : s);
        assertFalse(RecipeMatcher.evaluate(11, recipe(10), pantry, true, TODAY, 3).isReady());
        assertTrue(RecipeMatcher.evaluate(11, recipe(10), pantry, false, TODAY, 3).isReady());
    }

    @Test public void daysUntilExpiry() {
        assertEquals(Integer.valueOf(3), RecipeMatcher.daysUntilExpiry("2026-09-23", TODAY));
        assertEquals(Integer.valueOf(-1), RecipeMatcher.daysUntilExpiry("2026-09-19", TODAY));
        assertNull(RecipeMatcher.daysUntilExpiry(null, TODAY));
    }

    // ---- cooking -----------------------------------------------------------------------
    @Test public void cookingDeductsQuantities() {
        Map<Long, Double> plan = RecipeMatcher.planCooking(recipe(0), demoPantry(), true, TODAY);
        assertEquals(3.0, plan.get(1L), 1e-9);    // eggs 6 -> 3
        assertEquals(2.0, plan.get(2L), 1e-9);    // tomatoes 3 -> 2
        assertEquals(240.0, plan.get(4L), 1e-9);  // butter 250 -> 240
    }

    @Test public void cookingUsesEarliestExpiringStockFirst() {
        List<S> pantry = Arrays.asList(new S(1, "egg", 2, "pcs", "2026-09-25"), new S(2, "egg", 4, "pcs", "2026-09-21"));
        Map<Long, Double> plan = RecipeMatcher.planCooking(Arrays.asList(new R("eggs", 3, "pcs")), pantry, true, TODAY);
        assertEquals(1.0, plan.get(2L), 1e-9);
        assertFalse(plan.containsKey(1L));
    }
}
