package com.smartpantry.logic;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Turns free-text ingredient names into a canonical key so that "Tomatoes", "tomato" and
 * "fresh tomatoes (large)" all match. Steps: lower-case, strip accents/punctuation/brackets,
 * drop descriptive words, singularise each word, then apply a small synonym table.
 * Both the pantry name and the recipe name go through the same function, so what matters is
 * that variants of one ingredient end up with the same key.
 */
public final class IngredientNormalizer {

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "fresh", "large", "small", "medium", "ripe", "chopped", "diced", "sliced", "minced", "grated",
            "organic", "raw", "frozen", "mature", "extra", "virgin", "plain", "natural", "whole", "a", "the", "of"));

    private static final Map<String, String> IRREGULAR = new HashMap<>();
    private static final Map<String, String> ALIASES = new HashMap<>();

    private static void alias(String canonical, String... variants) {
        for (String v : variants) ALIASES.put(v, canonical);
    }

    static {
        IRREGULAR.put("leaves", "leaf"); IRREGULAR.put("loaves", "loaf");
        IRREGULAR.put("halves", "half"); IRREGULAR.put("knives", "knife");

        alias("pasta", "spaghetti", "penne", "macaroni", "fusilli", "linguine", "tagliatelle");
        alias("bell pepper", "capsicum", "green pepper", "red pepper", "yellow pepper", "sweet pepper");
        alias("spring onion", "scallion", "green onion");
        alias("yoghurt", "yogurt", "greek yogurt", "greek yoghurt");
        alias("cheddar cheese", "cheddar");
        alias("vegetable oil", "cooking oil", "sunflower oil", "canola oil", "rapeseed oil");
        alias("milk", "full cream milk", "skim milk", "skimmed milk", "low fat milk");
        alias("chicken breast", "chicken", "chicken fillet");
        alias("canned tomato", "tinned tomato", "tin tomato");
        alias("chickpea", "garbanzo", "garbanzo bean", "chick pea", "canned chickpea", "tinned chickpea");
        alias("canned tuna", "tuna", "tinned tuna");
        alias("coconut milk", "tinned coconut milk", "canned coconut milk");
        alias("chilli flake", "chili flake", "red pepper flake", "chilli");
        alias("zucchini", "courgette");
        alias("eggplant", "aubergine");
        alias("cilantro", "coriander");
        alias("ground beef", "mince");
    }

    private IngredientNormalizer() { }

    /** Canonical key for an ingredient name; never null. */
    public static String normalize(String name) {
        if (name == null) return "";
        String cleaned = Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\([^)]*\\)", " ")
                .replaceAll("[^a-z\\s]", " ");
        StringBuilder key = new StringBuilder();
        for (String token : cleaned.split("\\s+")) {
            if (token.isEmpty() || STOP_WORDS.contains(token)) continue;
            if (key.length() > 0) key.append(' ');
            key.append(singular(token));
        }
        String result = key.toString();
        String aliased = ALIASES.get(result);
        return aliased != null ? aliased : result;
    }

    static String singular(String w) {
        String irregular = IRREGULAR.get(w);
        if (irregular != null) return irregular;
        if (w.length() <= 3) return w;
        if (w.endsWith("ies")) return w.substring(0, w.length() - 3) + "y";   // berries -> berry
        if (w.endsWith("ie")) return w.substring(0, w.length() - 2) + "y";    // cookie -> cooky (same as cookies)
        if (w.endsWith("oes")) return w.substring(0, w.length() - 2);          // tomatoes -> tomato
        if (w.matches(".*(ch|sh|ss|x|z)es")) return w.substring(0, w.length() - 2); // radishes -> radish
        if (w.matches(".*(ss|us|is)")) return w;                               // hummus, watercress
        if (w.endsWith("s")) return w.substring(0, w.length() - 1);            // eggs -> egg
        return w;
    }
}
