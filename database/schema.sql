-- Smart Pantry Manager - SQLite schema
-- This is the same structure Room generates on the device from the @Entity classes in
-- android-app/app/src/main/java/com/smartpantry/data/. Use it to inspect or recreate the database
-- (e.g. in DB Browser for SQLite). The Android app does NOT need this file: Room creates the tables itself.

PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS pantry_items (
    id          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    name        TEXT    NOT NULL,
    quantity    REAL    NOT NULL,
    unit        TEXT    NOT NULL,
    expiryDate  TEXT,                      -- ISO yyyy-MM-dd, NULL when not entered
    addedAt     INTEGER NOT NULL           -- epoch milliseconds
);

CREATE TABLE IF NOT EXISTS recipes (
    id        INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    name      TEXT    NOT NULL,
    emoji     TEXT    NOT NULL,
    minutes   INTEGER NOT NULL,
    servings  INTEGER NOT NULL,
    steps     TEXT    NOT NULL             -- one preparation step per line
);

CREATE TABLE IF NOT EXISTS recipe_ingredients (
    id        INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    recipeId  INTEGER NOT NULL,
    name      TEXT    NOT NULL,
    quantity  REAL    NOT NULL,
    unit      TEXT    NOT NULL,
    FOREIGN KEY (recipeId) REFERENCES recipes (id) ON UPDATE NO ACTION ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_recipe_ingredients_recipeId ON recipe_ingredients (recipeId);
