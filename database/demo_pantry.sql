-- Demo pantry (18 items). Expiry dates are relative to the day you run this script.
-- Run schema.sql first.

INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Eggs', 6, 'pcs', date('now', '+2 day'), CAST(strftime('%s','now') AS INTEGER) * 1000);
INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Tomatoes', 3, 'pcs', date('now', '+5 day'), CAST(strftime('%s','now') AS INTEGER) * 1000);
INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Cheddar cheese', 200, 'g', date('now', '+12 day'), CAST(strftime('%s','now') AS INTEGER) * 1000);
INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Butter', 250, 'g', date('now', '+30 day'), CAST(strftime('%s','now') AS INTEGER) * 1000);
INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Bread', 6, 'pcs', date('now', '+2 day'), CAST(strftime('%s','now') AS INTEGER) * 1000);
INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Milk', 500, 'ml', date('now', '+1 day'), CAST(strftime('%s','now') AS INTEGER) * 1000);
INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Onions', 3, 'pcs', NULL, CAST(strftime('%s','now') AS INTEGER) * 1000);
INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Garlic', 6, 'pcs', NULL, CAST(strftime('%s','now') AS INTEGER) * 1000);
INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Pasta', 500, 'g', NULL, CAST(strftime('%s','now') AS INTEGER) * 1000);
INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Olive oil', 500, 'ml', NULL, CAST(strftime('%s','now') AS INTEGER) * 1000);
INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Salt', 200, 'g', NULL, CAST(strftime('%s','now') AS INTEGER) * 1000);
INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Rice', 1, 'kg', NULL, CAST(strftime('%s','now') AS INTEGER) * 1000);
INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Soy sauce', 250, 'ml', NULL, CAST(strftime('%s','now') AS INTEGER) * 1000);
INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Bananas', 2, 'pcs', date('now', '+3 day'), CAST(strftime('%s','now') AS INTEGER) * 1000);
INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Flour', 500, 'g', date('now', '+90 day'), CAST(strftime('%s','now') AS INTEGER) * 1000);
INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Chicken breast', 300, 'g', date('now', '+1 day'), CAST(strftime('%s','now') AS INTEGER) * 1000);
INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Potatoes', 4, 'pcs', date('now', '+14 day'), CAST(strftime('%s','now') AS INTEGER) * 1000);
INSERT INTO pantry_items (name, quantity, unit, expiryDate, addedAt) VALUES ('Mushrooms', 250, 'g', date('now', '-1 day'), CAST(strftime('%s','now') AS INTEGER) * 1000);
