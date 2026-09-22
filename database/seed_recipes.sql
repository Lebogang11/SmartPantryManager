-- 20 pre-loaded recipes with their ingredients and method (generated from web-preview/data/recipes.json).
-- Run schema.sql first.

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (1, 'Cheese and tomato omelette', '🍳', 10, 1, 'Dice the tomato and grate the cheese.
Beat the eggs in a bowl with a fork until smooth.
Melt the butter in a non-stick pan over medium heat and pour in the eggs.
When the edges set, scatter the tomato and cheese over one half.
Fold the omelette over, cook for 1 more minute and slide onto a plate.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (1, 'egg', 3, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (1, 'tomato', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (1, 'cheddar cheese', 30, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (1, 'butter', 10, 'g');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (2, 'Scrambled eggs on toast', '🍞', 10, 2, 'Whisk the eggs with the milk and salt.
Toast the bread while you heat half the butter in a pan on low heat.
Pour in the eggs and stir gently with a spatula until just set and creamy.
Butter the toast with the remaining butter and pile the eggs on top.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (2, 'egg', 4, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (2, 'bread', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (2, 'butter', 15, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (2, 'milk', 50, 'ml');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (2, 'salt', 0.5, 'tsp');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (3, 'Tomato pasta', '🍝', 20, 2, 'Boil the pasta in salted water until al dente, then drain.
Chop the onion, garlic and tomatoes.
Soften the onion in the olive oil for 4 minutes, then add the garlic for 30 seconds.
Add the tomatoes and simmer for 10 minutes until saucy.
Toss the pasta through the sauce and season with salt.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (3, 'pasta', 200, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (3, 'tomato', 4, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (3, 'onion', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (3, 'garlic', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (3, 'olive oil', 2, 'tbsp');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (3, 'salt', 1, 'tsp');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (4, 'Egg fried rice', '🍚', 15, 2, 'Cook the rice, spread it on a tray and let it cool.
Finely chop the onion.
Heat the oil in a wok, fry the onion for 2 minutes, then push it aside.
Scramble the eggs in the empty space, then mix everything together.
Add the rice and soy sauce and stir-fry over high heat for 3 minutes.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (4, 'rice', 150, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (4, 'egg', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (4, 'onion', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (4, 'soy sauce', 2, 'tbsp');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (4, 'vegetable oil', 1, 'tbsp');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (5, 'Chicken and rice bowl', '🍗', 30, 2, 'Cook the rice according to the packet instructions.
Slice the chicken into strips and chop the onion and garlic.
Fry the chicken in a hot pan for 6 minutes until golden and cooked through.
Add the onion and garlic and cook for 3 minutes, then stir in the soy sauce.
Serve the chicken over the rice.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (5, 'chicken breast', 300, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (5, 'rice', 200, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (5, 'onion', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (5, 'garlic', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (5, 'soy sauce', 2, 'tbsp');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (6, 'Spanish potato tortilla', '🥔', 35, 4, 'Peel and thinly slice the potatoes and onion.
Cook them slowly in the olive oil for 15 minutes until soft, not browned.
Beat the eggs with the salt and stir in the warm potato mixture.
Pour it back into the pan and cook on low heat for 5 minutes.
Flip using a plate and cook the other side for 3 minutes.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (6, 'potato', 3, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (6, 'egg', 4, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (6, 'onion', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (6, 'olive oil', 3, 'tbsp');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (6, 'salt', 1, 'tsp');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (7, 'Creamy mushroom pasta', '🍄', 25, 2, 'Boil the pasta until al dente.
Slice the mushrooms and crush the garlic.
Fry the mushrooms in the butter until golden, then add the garlic.
Pour in the cream and simmer for 3 minutes.
Toss with the pasta and serve.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (7, 'pasta', 200, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (7, 'mushroom', 200, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (7, 'cream', 100, 'ml');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (7, 'garlic', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (7, 'butter', 20, 'g');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (8, 'Banana pancakes', '🥞', 20, 2, 'Mash the bananas in a bowl.
Whisk in the eggs, then the flour and milk until you have a thick batter.
Melt a little butter in a pan over medium heat.
Cook spoonfuls of batter for 2 minutes per side until golden.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (8, 'banana', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (8, 'egg', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (8, 'flour', 100, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (8, 'milk', 150, 'ml');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (8, 'butter', 10, 'g');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (9, 'Vegetable stir-fry', '🥦', 15, 2, 'Slice the carrots and pepper, cut the broccoli into florets and crush the garlic.
Heat the oil in a wok until smoking.
Stir-fry the carrots and broccoli for 4 minutes, then add the pepper and garlic.
Add the soy sauce and toss for 1 minute before serving.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (9, 'carrot', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (9, 'bell pepper', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (9, 'broccoli', 200, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (9, 'soy sauce', 3, 'tbsp');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (9, 'garlic', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (9, 'vegetable oil', 1, 'tbsp');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (10, 'Red lentil soup', '🥣', 40, 4, 'Chop the onion, carrots and garlic.
Soften them in a large pot for 5 minutes, then stir in the cumin.
Add the rinsed lentils and the stock and bring to the boil.
Simmer for 25 minutes until the lentils are soft.
Blend part of the soup for a thicker texture and serve.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (10, 'lentil', 200, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (10, 'carrot', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (10, 'onion', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (10, 'garlic', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (10, 'vegetable stock', 750, 'ml');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (10, 'cumin', 1, 'tsp');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (11, 'Cheese toastie', '🧀', 10, 2, 'Butter one side of every slice of bread.
Layer the grated cheese between two slices, butter side out.
Toast in a hot pan for 3 minutes per side until golden and melted.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (11, 'bread', 4, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (11, 'cheddar cheese', 100, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (11, 'butter', 20, 'g');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (12, 'Chickpea curry', '🍛', 30, 3, 'Fry the chopped onion and garlic until soft.
Stir in the curry powder for 1 minute.
Add the tomatoes, coconut milk and drained chickpeas.
Simmer for 15 minutes until thick and serve with rice.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (12, 'chickpea', 1, 'can');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (12, 'canned tomato', 1, 'can');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (12, 'onion', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (12, 'garlic', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (12, 'curry powder', 2, 'tsp');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (12, 'coconut milk', 1, 'can');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (13, 'Cheesy baked potato', '🥔', 60, 2, 'Heat the oven to 200 °C and prick the potatoes with a fork.
Rub them with a little butter and salt and bake for 50 minutes.
Split the potatoes open and mash in the remaining butter.
Top with the grated cheese and return to the oven for 5 minutes.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (13, 'potato', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (13, 'cheddar cheese', 60, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (13, 'butter', 20, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (13, 'salt', 0.5, 'tsp');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (14, 'Guacamole on toast', '🥑', 10, 2, 'Mash the avocados with the lime juice and salt.
Dice the tomato and fold it through.
Toast the bread and spread the guacamole on top.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (14, 'avocado', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (14, 'lime', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (14, 'tomato', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (14, 'bread', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (14, 'salt', 0.5, 'tsp');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (15, 'Spaghetti aglio e olio', '🍝', 15, 2, 'Boil the pasta until al dente, saving a cup of the water.
Slice the garlic and warm it gently in the oil until fragrant.
Add the chilli flakes, then the pasta and a splash of pasta water.
Toss for 1 minute until glossy.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (15, 'pasta', 200, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (15, 'garlic', 4, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (15, 'olive oil', 4, 'tbsp');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (15, 'chilli flakes', 1, 'tsp');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (16, 'Yoghurt and banana parfait', '🍌', 5, 2, 'Slice the banana.
Layer yoghurt, oats and banana in two glasses.
Drizzle with honey and serve straight away.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (16, 'yoghurt', 300, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (16, 'banana', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (16, 'oats', 60, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (16, 'honey', 2, 'tbsp');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (17, 'Honey oat porridge', '🥣', 10, 1, 'Combine the oats and milk in a saucepan.
Cook over medium heat for 5 minutes, stirring often.
Serve drizzled with honey.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (17, 'oats', 80, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (17, 'milk', 300, 'ml');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (17, 'honey', 1, 'tbsp');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (18, 'Tuna pasta salad', '🐟', 20, 2, 'Boil the pasta, drain and rinse under cold water.
Drain the tuna and dice the tomatoes.
Mix everything with the mayonnaise and chill until ready to serve.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (18, 'pasta', 150, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (18, 'canned tuna', 1, 'can');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (18, 'mayonnaise', 2, 'tbsp');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (18, 'tomato', 2, 'pcs');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (19, 'Spinach and pepper omelette', '🫑', 15, 1, 'Finely dice the pepper and onion.
Soften them in the butter for 3 minutes, then wilt the spinach.
Pour over the beaten eggs and cook until set.
Fold and serve.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (19, 'egg', 3, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (19, 'bell pepper', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (19, 'onion', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (19, 'spinach', 50, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (19, 'butter', 10, 'g');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (20, 'Shakshuka', '🍅', 30, 2, 'Fry the chopped onion and pepper for 5 minutes, then add the garlic and cumin.
Pour in the tomatoes and simmer for 10 minutes.
Make four wells in the sauce and crack in the eggs.
Cover and cook for 5 minutes until the whites are set.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (20, 'egg', 4, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (20, 'canned tomato', 1, 'can');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (20, 'onion', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (20, 'bell pepper', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (20, 'garlic', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (20, 'cumin', 1, 'tsp');
