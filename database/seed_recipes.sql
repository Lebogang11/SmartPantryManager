-- 20 pre-loaded recipes with their ingredients and method
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


INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (6, 'Creamy Spinach', '🥬', 20, 2, 'Wash and chop the spinach.
Cook the onion and garlic in butter until soft.
Add the spinach and cook until wilted.
Add the cream and stir well.
Season with salt and simmer for 5 minutes.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (6, 'spinach', 200, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (6, 'cream', 1, 'cup');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (6, 'onion', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (6, 'garlic', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (6, 'butter', 1, 'tbsp');
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


INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (10, 'Beef Stew', '🥩', 90, 4, 'Cut the beef, potatoes and carrots into small pieces.
Brown the beef in a large pot.
Add the onion and garlic and cook until soft.
Add the potatoes, carrots and beef stock.
Cover and simmer for about 60 minutes until the beef is tender.
Season with salt and serve warm.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (10, 'beef', 500, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (10, 'potato', 3, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (10, 'carrot', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (10, 'onion', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (10, 'garlic', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (10, 'beef stock', 500, 'ml');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (10, 'salt', 1, 'tsp');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (11, 'Chicken Wrap', '🌯', 20, 2, 'Cook the chicken until fully cooked and lightly browned.
Slice the chicken and tomato into small pieces.
Spread mayonnaise over each wrap.
Add the lettuce, tomato and chicken.
Season with salt, then roll the wraps tightly and serve.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (11, 'chicken', 250, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (11, 'wrap', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (11, 'lettuce', 100, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (11, 'tomato', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (11, 'mayonnaise', 2, 'tbsp');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (11, 'salt', 1, 'tsp');

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (12, 'Mashed Potatoes', '🥔', 25, 3, 'Peel and cut the potatoes into small pieces.
Boil the potatoes for 15 to 20 minutes until soft.
Drain the potatoes and mash them until smooth.
Add the butter and milk and mix well.
Season with salt and serve warm.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (12, 'potato', 500, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (12, 'butter', 2, 'tbsp');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (12, 'milk', 100, 'ml');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (12, 'salt', 1, 'tsp');

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


INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (15, 'Samp', '🌽', 120, 4, 'Rinse the samp and soak it in water for a few hours.
Drain the samp and add it to a large pot with fresh water.
Boil until the samp becomes soft.
Add the onion and butter and cook for another 10 minutes.
Season with salt and serve warm.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (15, 'samp', 500, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (15, 'onion', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (15, 'butter', 2, 'tbsp');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (15, 'salt', 1, 'tsp');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (15, 'water', 1000, 'ml');

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

INSERT INTO recipes (id, name, emoji, minutes, servings, steps) VALUES (20, 'Mince with Vegetables', '🥘', 35, 4, 'Chop the onion, carrots, tomatoes and garlic.
Fry the onion and garlic until soft.
Add the mince and cook until browned.
Add the carrots, peas and tomatoes.
Cover and simmer for 15 minutes until the vegetables are tender.
Season with salt and serve warm.');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (20, 'beef mince', 500, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (20, 'onion', 1, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (20, 'carrot', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (20, 'peas', 100, 'g');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (20, 'tomato', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (20, 'garlic', 2, 'pcs');
INSERT INTO recipe_ingredients (recipeId, name, quantity, unit) VALUES (20, 'salt', 1, 'tsp');