# Smart Pantry Manager

Smart Pantry Manager is an Android app written in Java for the Mobile App Development 700 practical
assignment. It helps a user keep track of the ingredients they have at home and suggests recipes based
strictly on that pantry — a recipe is only suggested if every ingredient it needs is already in the
pantry, in enough quantity. The idea is to help reduce food waste by making it easy to see what can
actually be cooked right now, instead of what's missing.

## Features

- Add, edit and delete pantry items (name, quantity, unit, optional expiry date)
- Pantry list with search, filtering (all / expiring soon / expired) and sorting
- 20 recipes pre-loaded into the database on first run
- Suggested Recipes screen that only shows recipes the pantry can fully cover
- A separate "Almost there" tab for recipes that are missing exactly one ingredient
- Recipe detail screen showing which ingredients are covered and which are missing
- "Mark as cooked" — deducts the used ingredients from the pantry
- Settings screen (expiry alerts, alert window, ignore-expired toggle, default unit)
- Basic input validation on the add/edit form

## Tech Stack

- Java (Android, minSdk 26, targetSdk/compileSdk 34)
- Android Studio / Gradle
- Room (SQLite) for persistence
- AndroidX AppCompat, Material Components, ConstraintLayout, RecyclerView
- JUnit for testing the matching logic

## Database

I used **Room (SQLite)** for storage, which is one of the three options allowed in the brief. I picked
it over Firebase or PostgreSQL because the app doesn't need any of the things a server would give me —
no accounts, no syncing between devices, no internet connection at all. A pantry app is something you'd
use in your kitchen, so it made sense to keep everything local. Room also generates the SQL from
annotations and checks queries at compile time, which is a lot less error-prone than writing
`SQLiteOpenHelper` code by hand.

There are three tables:

- `pantry_items` — the user's ingredients (name, quantity, unit, expiry date, date added)
- `recipes` — the seeded recipes (name, emoji, cook time, servings, method)
- `recipe_ingredients` — the ingredients each recipe needs, linked to `recipes` by a foreign key
  (`recipeId`, cascade delete)

`pantry_items` isn't linked to `recipe_ingredients` by a foreign key — they're matched at runtime by
comparing normalised ingredient names, since the same ingredient can be written differently in each place
(e.g. "Tomatoes" vs "tomato").

## Project Structure

```
app/src/main/java/com/smartpantry/
├── ui/        Activities (Pantry List, Add/Edit, Suggested Recipes, Recipe Detail, Settings)
├── adapter/   RecyclerView adapters
├── data/      Room entities, DAOs, database, seed data
├── logic/     the matching engine (RecipeMatcher, IngredientNormalizer, UnitConverter)
└── util/      small helpers (Formatters, SettingsManager)

app/src/main/res/   layouts, drawables, strings, theme
app/src/test/       unit tests for the matching engine
```

The `logic` package doesn't depend on any Android classes, which is why it can be tested with plain
JUnit instead of needing an emulator.

## Getting Started

**You'll need:** Android Studio (a recent version) with SDK Platform 34 installed.

1. Clone the repo and open the project folder in Android Studio (`File > Open`).
2. Let Gradle sync — it will download Room, Material Components and the other dependencies.
3. Run the app on an emulator or a physical device (API 26+).
4. The pantry starts empty. Tap **Load demo pantry** from the empty state, or from Settings, to load
   18 sample ingredients and try out the recipe matching straight away.

To build from the command line:

```
./gradlew assembleDebug        # builds the debug APK
./gradlew testDebugUnitTest    # runs the matching-engine unit tests
```

## Running Tests

The matching logic (`logic/RecipeMatcherTest.java`) has unit tests covering the strict-matching rule,
ingredient name normalisation, unit conversion, expiry handling and the "mark as cooked" deduction. Run
them from Android Studio by right-clicking the test class, or with `./gradlew testDebugUnitTest`.

## Known Limitations

- The recipe collection is fixed — there's no way to add your own recipes yet.
- The ingredient synonym and unit-conversion lists only cover what the 20 seeded recipes need.
- No login/accounts — it's built for a single user on one device.
- No maps, location or payment features, since none of that is needed for a pantry app.


