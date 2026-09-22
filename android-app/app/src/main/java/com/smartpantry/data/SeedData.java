package com.smartpantry.data;

import java.util.Arrays;
import java.util.List;

/**
 * Seed data for first run. Plain Java (no Android classes) so it can be read by the seeder and
 * by unit tests alike. The 20 recipes are written to the Room database the first time the app runs.
 */
public final class SeedData {

    private SeedData() { }

    /** One ingredient line of a seed recipe. */
    public static final class Ing {
        public final String name; public final double quantity; public final String unit;
        Ing(String name, double quantity, String unit) { this.name = name; this.quantity = quantity; this.unit = unit; }
    }

    /** A seed recipe. Steps are stored one per array element. */
    public static final class SeedRecipe {
        public final String name, emoji; public final int minutes, servings;
        public final List<Ing> ingredients; public final List<String> steps;
        SeedRecipe(String name, String emoji, int minutes, int servings, List<Ing> ingredients, List<String> steps) {
            this.name = name; this.emoji = emoji; this.minutes = minutes; this.servings = servings;
            this.ingredients = ingredients; this.steps = steps;
        }
    }

    /** A demo pantry entry; expiryOffsetDays is relative to today (null = no expiry date). */
    public static final class DemoItem {
        public final String name; public final double quantity; public final String unit; public final Integer expiryOffsetDays;
        DemoItem(String name, double quantity, String unit, Integer expiryOffsetDays) {
            this.name = name; this.quantity = quantity; this.unit = unit; this.expiryOffsetDays = expiryOffsetDays;
        }
    }

    private static Ing ing(String n, double q, String u) { return new Ing(n, q, u); }

    public static final List<SeedRecipe> RECIPES = Arrays.asList(
            new SeedRecipe("Cheese and tomato omelette", "🍳", 10, 1,
                Arrays.asList(
                    ing("egg", 3.0, "pcs"),
                    ing("tomato", 1.0, "pcs"),
                    ing("cheddar cheese", 30.0, "g"),
                    ing("butter", 10.0, "g")),
                Arrays.asList(
                    "Dice the tomato and grate the cheese.",
                    "Beat the eggs in a bowl with a fork until smooth.",
                    "Melt the butter in a non-stick pan over medium heat and pour in the eggs.",
                    "When the edges set, scatter the tomato and cheese over one half.",
                    "Fold the omelette over, cook for 1 more minute and slide onto a plate.")),
            new SeedRecipe("Scrambled eggs on toast", "🍞", 10, 2,
                Arrays.asList(
                    ing("egg", 4.0, "pcs"),
                    ing("bread", 2.0, "pcs"),
                    ing("butter", 15.0, "g"),
                    ing("milk", 50.0, "ml"),
                    ing("salt", 0.5, "tsp")),
                Arrays.asList(
                    "Whisk the eggs with the milk and salt.",
                    "Toast the bread while you heat half the butter in a pan on low heat.",
                    "Pour in the eggs and stir gently with a spatula until just set and creamy.",
                    "Butter the toast with the remaining butter and pile the eggs on top.")),
            new SeedRecipe("Tomato pasta", "🍝", 20, 2,
                Arrays.asList(
                    ing("pasta", 200.0, "g"),
                    ing("tomato", 4.0, "pcs"),
                    ing("onion", 1.0, "pcs"),
                    ing("garlic", 2.0, "pcs"),
                    ing("olive oil", 2.0, "tbsp"),
                    ing("salt", 1.0, "tsp")),
                Arrays.asList(
                    "Boil the pasta in salted water until al dente, then drain.",
                    "Chop the onion, garlic and tomatoes.",
                    "Soften the onion in the olive oil for 4 minutes, then add the garlic for 30 seconds.",
                    "Add the tomatoes and simmer for 10 minutes until saucy.",
                    "Toss the pasta through the sauce and season with salt.")),
            new SeedRecipe("Egg fried rice", "🍚", 15, 2,
                Arrays.asList(
                    ing("rice", 150.0, "g"),
                    ing("egg", 2.0, "pcs"),
                    ing("onion", 1.0, "pcs"),
                    ing("soy sauce", 2.0, "tbsp"),
                    ing("vegetable oil", 1.0, "tbsp")),
                Arrays.asList(
                    "Cook the rice, spread it on a tray and let it cool.",
                    "Finely chop the onion.",
                    "Heat the oil in a wok, fry the onion for 2 minutes, then push it aside.",
                    "Scramble the eggs in the empty space, then mix everything together.",
                    "Add the rice and soy sauce and stir-fry over high heat for 3 minutes.")),
            new SeedRecipe("Chicken and rice bowl", "🍗", 30, 2,
                Arrays.asList(
                    ing("chicken breast", 300.0, "g"),
                    ing("rice", 200.0, "g"),
                    ing("onion", 1.0, "pcs"),
                    ing("garlic", 2.0, "pcs"),
                    ing("soy sauce", 2.0, "tbsp")),
                Arrays.asList(
                    "Cook the rice according to the packet instructions.",
                    "Slice the chicken into strips and chop the onion and garlic.",
                    "Fry the chicken in a hot pan for 6 minutes until golden and cooked through.",
                    "Add the onion and garlic and cook for 3 minutes, then stir in the soy sauce.",
                    "Serve the chicken over the rice.")),
            new SeedRecipe("Spanish potato tortilla", "🥔", 35, 4,
                Arrays.asList(
                    ing("potato", 3.0, "pcs"),
                    ing("egg", 4.0, "pcs"),
                    ing("onion", 1.0, "pcs"),
                    ing("olive oil", 3.0, "tbsp"),
                    ing("salt", 1.0, "tsp")),
                Arrays.asList(
                    "Peel and thinly slice the potatoes and onion.",
                    "Cook them slowly in the olive oil for 15 minutes until soft, not browned.",
                    "Beat the eggs with the salt and stir in the warm potato mixture.",
                    "Pour it back into the pan and cook on low heat for 5 minutes.",
                    "Flip using a plate and cook the other side for 3 minutes.")),
            new SeedRecipe("Creamy mushroom pasta", "🍄", 25, 2,
                Arrays.asList(
                    ing("pasta", 200.0, "g"),
                    ing("mushroom", 200.0, "g"),
                    ing("cream", 100.0, "ml"),
                    ing("garlic", 2.0, "pcs"),
                    ing("butter", 20.0, "g")),
                Arrays.asList(
                    "Boil the pasta until al dente.",
                    "Slice the mushrooms and crush the garlic.",
                    "Fry the mushrooms in the butter until golden, then add the garlic.",
                    "Pour in the cream and simmer for 3 minutes.",
                    "Toss with the pasta and serve.")),
            new SeedRecipe("Banana pancakes", "🥞", 20, 2,
                Arrays.asList(
                    ing("banana", 2.0, "pcs"),
                    ing("egg", 2.0, "pcs"),
                    ing("flour", 100.0, "g"),
                    ing("milk", 150.0, "ml"),
                    ing("butter", 10.0, "g")),
                Arrays.asList(
                    "Mash the bananas in a bowl.",
                    "Whisk in the eggs, then the flour and milk until you have a thick batter.",
                    "Melt a little butter in a pan over medium heat.",
                    "Cook spoonfuls of batter for 2 minutes per side until golden.")),
            new SeedRecipe("Vegetable stir-fry", "🥦", 15, 2,
                Arrays.asList(
                    ing("carrot", 2.0, "pcs"),
                    ing("bell pepper", 1.0, "pcs"),
                    ing("broccoli", 200.0, "g"),
                    ing("soy sauce", 3.0, "tbsp"),
                    ing("garlic", 2.0, "pcs"),
                    ing("vegetable oil", 1.0, "tbsp")),
                Arrays.asList(
                    "Slice the carrots and pepper, cut the broccoli into florets and crush the garlic.",
                    "Heat the oil in a wok until smoking.",
                    "Stir-fry the carrots and broccoli for 4 minutes, then add the pepper and garlic.",
                    "Add the soy sauce and toss for 1 minute before serving.")),
            new SeedRecipe("Red lentil soup", "🥣", 40, 4,
                Arrays.asList(
                    ing("lentil", 200.0, "g"),
                    ing("carrot", 2.0, "pcs"),
                    ing("onion", 1.0, "pcs"),
                    ing("garlic", 2.0, "pcs"),
                    ing("vegetable stock", 750.0, "ml"),
                    ing("cumin", 1.0, "tsp")),
                Arrays.asList(
                    "Chop the onion, carrots and garlic.",
                    "Soften them in a large pot for 5 minutes, then stir in the cumin.",
                    "Add the rinsed lentils and the stock and bring to the boil.",
                    "Simmer for 25 minutes until the lentils are soft.",
                    "Blend part of the soup for a thicker texture and serve.")),
            new SeedRecipe("Cheese toastie", "🧀", 10, 2,
                Arrays.asList(
                    ing("bread", 4.0, "pcs"),
                    ing("cheddar cheese", 100.0, "g"),
                    ing("butter", 20.0, "g")),
                Arrays.asList(
                    "Butter one side of every slice of bread.",
                    "Layer the grated cheese between two slices, butter side out.",
                    "Toast in a hot pan for 3 minutes per side until golden and melted.")),
            new SeedRecipe("Chickpea curry", "🍛", 30, 3,
                Arrays.asList(
                    ing("chickpea", 1.0, "can"),
                    ing("canned tomato", 1.0, "can"),
                    ing("onion", 1.0, "pcs"),
                    ing("garlic", 2.0, "pcs"),
                    ing("curry powder", 2.0, "tsp"),
                    ing("coconut milk", 1.0, "can")),
                Arrays.asList(
                    "Fry the chopped onion and garlic until soft.",
                    "Stir in the curry powder for 1 minute.",
                    "Add the tomatoes, coconut milk and drained chickpeas.",
                    "Simmer for 15 minutes until thick and serve with rice.")),
            new SeedRecipe("Cheesy baked potato", "🥔", 60, 2,
                Arrays.asList(
                    ing("potato", 2.0, "pcs"),
                    ing("cheddar cheese", 60.0, "g"),
                    ing("butter", 20.0, "g"),
                    ing("salt", 0.5, "tsp")),
                Arrays.asList(
                    "Heat the oven to 200 °C and prick the potatoes with a fork.",
                    "Rub them with a little butter and salt and bake for 50 minutes.",
                    "Split the potatoes open and mash in the remaining butter.",
                    "Top with the grated cheese and return to the oven for 5 minutes.")),
            new SeedRecipe("Guacamole on toast", "🥑", 10, 2,
                Arrays.asList(
                    ing("avocado", 2.0, "pcs"),
                    ing("lime", 1.0, "pcs"),
                    ing("tomato", 1.0, "pcs"),
                    ing("bread", 2.0, "pcs"),
                    ing("salt", 0.5, "tsp")),
                Arrays.asList(
                    "Mash the avocados with the lime juice and salt.",
                    "Dice the tomato and fold it through.",
                    "Toast the bread and spread the guacamole on top.")),
            new SeedRecipe("Spaghetti aglio e olio", "🍝", 15, 2,
                Arrays.asList(
                    ing("pasta", 200.0, "g"),
                    ing("garlic", 4.0, "pcs"),
                    ing("olive oil", 4.0, "tbsp"),
                    ing("chilli flakes", 1.0, "tsp")),
                Arrays.asList(
                    "Boil the pasta until al dente, saving a cup of the water.",
                    "Slice the garlic and warm it gently in the oil until fragrant.",
                    "Add the chilli flakes, then the pasta and a splash of pasta water.",
                    "Toss for 1 minute until glossy.")),
            new SeedRecipe("Yoghurt and banana parfait", "🍌", 5, 2,
                Arrays.asList(
                    ing("yoghurt", 300.0, "g"),
                    ing("banana", 1.0, "pcs"),
                    ing("oats", 60.0, "g"),
                    ing("honey", 2.0, "tbsp")),
                Arrays.asList(
                    "Slice the banana.",
                    "Layer yoghurt, oats and banana in two glasses.",
                    "Drizzle with honey and serve straight away.")),
            new SeedRecipe("Honey oat porridge", "🥣", 10, 1,
                Arrays.asList(
                    ing("oats", 80.0, "g"),
                    ing("milk", 300.0, "ml"),
                    ing("honey", 1.0, "tbsp")),
                Arrays.asList(
                    "Combine the oats and milk in a saucepan.",
                    "Cook over medium heat for 5 minutes, stirring often.",
                    "Serve drizzled with honey.")),
            new SeedRecipe("Tuna pasta salad", "🐟", 20, 2,
                Arrays.asList(
                    ing("pasta", 150.0, "g"),
                    ing("canned tuna", 1.0, "can"),
                    ing("mayonnaise", 2.0, "tbsp"),
                    ing("tomato", 2.0, "pcs")),
                Arrays.asList(
                    "Boil the pasta, drain and rinse under cold water.",
                    "Drain the tuna and dice the tomatoes.",
                    "Mix everything with the mayonnaise and chill until ready to serve.")),
            new SeedRecipe("Spinach and pepper omelette", "🫑", 15, 1,
                Arrays.asList(
                    ing("egg", 3.0, "pcs"),
                    ing("bell pepper", 1.0, "pcs"),
                    ing("onion", 1.0, "pcs"),
                    ing("spinach", 50.0, "g"),
                    ing("butter", 10.0, "g")),
                Arrays.asList(
                    "Finely dice the pepper and onion.",
                    "Soften them in the butter for 3 minutes, then wilt the spinach.",
                    "Pour over the beaten eggs and cook until set.",
                    "Fold and serve.")),
            new SeedRecipe("Shakshuka", "🍅", 30, 2,
                Arrays.asList(
                    ing("egg", 4.0, "pcs"),
                    ing("canned tomato", 1.0, "can"),
                    ing("onion", 1.0, "pcs"),
                    ing("bell pepper", 1.0, "pcs"),
                    ing("garlic", 2.0, "pcs"),
                    ing("cumin", 1.0, "tsp")),
                Arrays.asList(
                    "Fry the chopped onion and pepper for 5 minutes, then add the garlic and cumin.",
                    "Pour in the tomatoes and simmer for 10 minutes.",
                    "Make four wells in the sauce and crack in the eggs.",
                    "Cover and cook for 5 minutes until the whites are set."))
    );

    public static final List<DemoItem> DEMO_PANTRY = Arrays.asList(
            new DemoItem("Eggs", 6.0, "pcs", 2),
            new DemoItem("Tomatoes", 3.0, "pcs", 5),
            new DemoItem("Cheddar cheese", 200.0, "g", 12),
            new DemoItem("Butter", 250.0, "g", 30),
            new DemoItem("Bread", 6.0, "pcs", 2),
            new DemoItem("Milk", 500.0, "ml", 1),
            new DemoItem("Onions", 3.0, "pcs", null),
            new DemoItem("Garlic", 6.0, "pcs", null),
            new DemoItem("Pasta", 500.0, "g", null),
            new DemoItem("Olive oil", 500.0, "ml", null),
            new DemoItem("Salt", 200.0, "g", null),
            new DemoItem("Rice", 1.0, "kg", null),
            new DemoItem("Soy sauce", 250.0, "ml", null),
            new DemoItem("Bananas", 2.0, "pcs", 3),
            new DemoItem("Flour", 500.0, "g", 90),
            new DemoItem("Chicken breast", 300.0, "g", 1),
            new DemoItem("Potatoes", 4.0, "pcs", 14),
            new DemoItem("Mushrooms", 250.0, "g", -1)
    );
}
