-- Handy queries for the video / report. Run after schema.sql, seed_recipes.sql and demo_pantry.sql.

-- CRUD on the pantry (same operations the DAO performs)
INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Lemons', 4, 'pcs', date('now','+5 day'), CAST(strftime('%s','now') AS INTEGER) * 1000); -- Create
SELECT * FROM pantry_items ORDER BY name COLLATE NOCASE;                                                        -- Read
UPDATE pantry_items SET quantity = 6 WHERE name = 'Lemons';                                                     -- Update
DELETE FROM pantry_items WHERE name = 'Lemons';                                                                 -- Delete

-- Ingredients of one recipe (one-to-many)
SELECT r.name, i.name AS ingredient, i.quantity, i.unit
FROM recipes r JOIN recipe_ingredients i ON i.recipeId = r.id
WHERE r.name = 'Cheese toastie';

-- Items expiring within 3 days, and items already expired
SELECT name, expiryDate FROM pantry_items
WHERE expiryDate IS NOT NULL AND expiryDate BETWEEN date('now','localtime') AND date('now','localtime','+3 day');
SELECT name, expiryDate FROM pantry_items WHERE expiryDate < date('now','localtime');

-- NOTE: the strict-matching rule (name normalisation + unit conversion) lives in Java, in
-- logic/RecipeMatcher.java, because it needs plural handling, synonyms and unit maths that plain SQL cannot do cleanly.
