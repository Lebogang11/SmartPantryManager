# Smart Pantry Manager

A Java Android app that suggests recipes **strictly** from the ingredients a user already has at home,
to cut food waste. Built for *Mobile App Development 700 – Practical Assignment* (Richfield).

A recipe is suggested **only if every ingredient it needs is in the pantry, in at least the required
quantity**. Four of five ingredients means it is *not* suggested.

This package contains the complete platform in three runnable parts:

| Part | Folder | What it is | Needs |
|------|--------|-----------|-------|
| **Android app** (the graded deliverable) | `android-app/` | Java + Room (SQLite) Android Studio project | Android Studio, JDK 17 |
| **Interactive web preview** | `web-preview/` | The same screens, data model and matching logic as one HTML file, so you can try everything without Android Studio | Any modern browser |
| **Tests, database scripts, docs** | `tests/`, `database/`, `docs/` | Automated tests, SQL scripts, design docs | Node 18+, JDK 17 (optional) |

> **Is there a backend / server?** No, and none is needed. The assignment (section 3.2) lets you pick
> **one** of SQLite, Firebase or PostgreSQL. This project uses **SQLite through Room**, which runs on the
> device, so there is no server, no API keys and no internet requirement. See “Why SQLite (Room)?” below.

---

## Folder structure

```
SmartPantryManager_Complete/
├── README.md
├── android-app/                     <- open THIS folder in Android Studio
│   ├── settings.gradle, build.gradle, gradle.properties, gradle/wrapper/
│   └── app/
│       ├── build.gradle
│       └── src/
│           ├── main/
│           │   ├── AndroidManifest.xml
│           │   ├── java/com/smartpantry/
│           │   │   ├── ui/          5 Activities + BaseActivity (bottom navigation, Intents)
│           │   │   ├── adapter/     PantryAdapter, RecipeAdapter, RecipeIngredientAdapter (RecyclerView)
│           │   │   ├── data/        Room: entities, DAOs, AppDatabase, seeder, SeedData (20 recipes)
│           │   │   ├── logic/       THE STRICT-MATCHING ENGINE (pure Java, unit-tested)
│           │   │   └── util/        SettingsManager (SharedPreferences), Formatters
│           │   └── res/             layouts, drawables, menu, strings, colours, theme
│           └── test/                JUnit tests for the matching engine
├── web-preview/
│   ├── index.html                   <- double-click to run (self-contained)
│   ├── src/                         template.html + engine.js (edit these, then run build.py)
│   ├── data/                        recipes.json, demo_pantry.json
│   ├── build.py, serve.py
├── database/
│   ├── schema.sql, seed_recipes.sql, demo_pantry.sql, sample_queries.sql
├── tests/
│   ├── run_all.sh / run_all.bat     runs every automated test
│   ├── web/                         JS engine tests + simulated-browser UI tests
│   └── java-engine/                 plain-Java tests of the Android matching engine
└── docs/
    ├── requirements_traceability.md   PDF requirement -> where it is implemented
    ├── system_design.md               screen flow + ER diagram (for the report)
    └── demo_script.md                 5–7 minute video plan
```

---

## 1. Run the interactive web preview (fastest way to try it)

1. Open `web-preview/index.html` in Chrome, Edge, Firefox or Safari (double-click it).
   - Or: `cd web-preview && python3 serve.py` and open <http://localhost:8080>.
2. On a desktop you see the phone on the right and a **demo guide** on the left. On a phone/tablet the app fills the screen.
3. The preview starts with 18 demo ingredients and 20 recipes. Everything is real: adding, editing,
   deleting and cooking change the stored data and the recipe suggestions immediately. Data is kept in the
   browser (`localStorage`) and survives a page reload. Use **Restart** in the guide to simulate closing and reopening the app.

Rebuild after editing `src/` or `data/`: `cd web-preview && python3 build.py`.

## 2. Run the Android app (Java)

**Prerequisites:** Android Studio (Koala 2024.1 or newer) with the Android SDK Platform 34 installed; it bundles JDK 17.

1. **File ▸ Open…** and choose the **`android-app`** folder (not the outer folder).
2. Wait for **Gradle sync** to finish. Android Studio downloads Gradle 8.7, the Android Gradle Plugin 8.5.2,
   Room, Material and AndroidX from the internet on first sync (internet needed once).
   - If it offers to create/upgrade the Gradle wrapper or “Trust project”, accept.
   - If Studio shows a different SDK path error, it will create `local.properties` for you.
3. Create an emulator (**Device Manager ▸ Create device**, e.g. Pixel 7, API 34) or plug in a phone with USB debugging on.
4. Press **Run ▶**. The app opens on **My pantry**.
5. First run: the 20 recipes are written to the database automatically. The pantry starts empty; tap
   **Load demo pantry** (empty-state button or Settings) to get 18 sample ingredients.

Command line (optional, needs the Gradle wrapper — Studio generates it, or run `gradle wrapper --gradle-version 8.7` once):
```
cd android-app
./gradlew assembleDebug        # builds app/build/outputs/apk/debug/app-debug.apk
./gradlew testDebugUnitTest    # runs the JUnit matching-engine tests
```

**Persistence check:** add an ingredient, swipe the app away (or press Stop in Studio) and reopen it: the
data is still there because it is stored in the SQLite file `smart_pantry.db`.

## 3. Run the tests

```
cd tests
bash run_all.sh       # macOS / Linux          (Windows: run_all.bat)
```
It runs three suites and needs Node 18+ and a JDK with `javac` on your PATH:

1. **JavaScript engine tests** – 38 checks of the matching rules (plurals, synonyms, units, batches, expiry, cooking).
2. **Web UI tests** – 38 checks in a simulated browser (jsdom): search, filters, validation errors,
   create/update/delete + undo, strict list vs “Almost there”, cooking, settings, empty states, persistence after restart.
3. **Java engine tests** – 31 checks running the *same Java classes the Android app uses* (`logic/`) against the seed recipes.

In Android Studio you can also right-click `app/src/test/.../RecipeMatcherTest` ▸ **Run** (13 JUnit tests, no emulator needed).

## 4. Database

| Table | Purpose |
|-------|---------|
| `pantry_items` | The user's ingredients: name, quantity, unit, optional `expiryDate`, `addedAt` |
| `recipes` | 20 seeded recipes: name, emoji, minutes, servings, steps |
| `recipe_ingredients` | Ingredient lines of each recipe (FK → `recipes`, cascade delete) |

`database/*.sql` recreates the same schema and data in any SQLite tool (e.g. DB Browser for SQLite):
run `schema.sql`, then `seed_recipes.sql`, then `demo_pantry.sql`. The Android app does not need these
files; Room creates the tables itself and `DatabaseSeeder` inserts the recipes on first run.

To inspect the real on-device database: Android Studio ▸ **View ▸ Tool Windows ▸ App Inspection ▸ Database Inspector**.

### Why SQLite (Room)?
It is local, needs no account or server, works offline (a pantry is used in the kitchen), is covered by the
module’s persistent-data chapter, and Room adds compile-time-checked SQL, typed DAOs and easy one-to-many
relations (recipe → ingredients). Firebase would add cloud sync the assignment does not require;
PostgreSQL would require building and hosting a REST backend.

## 5. How the strict-matching rule works

Implemented in `logic/RecipeMatcher.java` (mirrored in `web-preview/src/engine.js`):

1. **Normalise names** (`IngredientNormalizer`): lower-case, drop words like *fresh/large/chopped*, singularise
   (`Tomatoes → tomato`), apply synonyms (`spaghetti → pasta`, `capsicum → bell pepper`, `yogurt → yoghurt`).
2. **Group** all usable pantry entries by that key and **add up** separate batches.
3. **Convert units** (`UnitConverter`): g/kg, ml/l/tsp/tbsp/cup, pcs/can; across dimensions only when a known
   density or piece weight exists (e.g. 1 tsp salt vs 200 g salt). If it cannot be compared it says so instead of guessing.
4. **Check every ingredient**: covered only if pantry amount ≥ required amount. Exactly enough passes; one gram less fails.
5. A recipe is **ready only if all ingredients are covered**. Recipes missing exactly one ingredient go to the
   separate, optional **Almost there** tab and never mix into the strict list.
6. Ready recipes that use up ingredients about to expire are shown first. Expired items can be ignored (Settings).

## 6. Features vs the assignment brief

Full table in `docs/requirements_traceability.md`. In short: pantry add/edit/delete with validation; RecyclerView
pantry list with custom adapter bound to Room; 20 seeded recipes; strict Suggested Recipes screen with
empty-state message; recipe detail; settings screen; bottom navigation; Intents passing ids/prefill data;
persistent CRUD; optional “Almost there” list. **No maps, GPS/location, payments or network access** (the manifest requests no permissions).

Extras beyond the brief: expiry alerts banner, search/filter/sort, undo after delete, “Mark as cooked”
(deducts ingredients earliest-expiry first), “Add missing ingredient” shortcut from recipe detail, and a
“How this was checked” explanation of each match.

## 7. What was and wasn’t verified

Be aware of this before you rely on the package:

- ✅ **Verified by running:** the web preview (38 automated UI checks + 38 engine checks), the Java matching engine
  (31 checks; compiled with a real JDK), the 13 JUnit tests (run through a minimal JUnit stand-in), and the SQL
  scripts (executed in SQLite). The JS and Java engines produce identical results on all 20 recipes.
- ⚠️ **Not built or run here:** the Android UI layer (Activities, adapters, XML layouts, Room wiring). There is no
  Android SDK in the environment that produced this package, so it has **not** been compiled or run on an emulator.
  It was checked for well-formed XML, for every `R.id/R.string/R.layout/R.drawable/R.color` reference resolving,
  and for syntax errors. Expect to open it in Android Studio, let Gradle sync, and possibly fix small
  compile or layout issues (for example a dependency version prompt) — that is normal for a first build.
- Dependency versions (AGP 8.5.2, Gradle 8.7, Room 2.6.1, Material 1.12.0, compileSdk 34) are a known-compatible set;
  if Studio proposes an upgrade you can accept it.

## 8. Troubleshooting

| Problem | Fix |
|---------|-----|
| “Unsupported class file major version” / JDK error | Studio ▸ Settings ▸ Build ▸ Gradle ▸ Gradle JDK = **JDK 17** (the bundled JBR) |
| `SDK location not found` | Studio creates `local.properties`; or add `sdk.dir=/path/to/Android/sdk` |
| Room error “Cannot find setter/getter” | Rebuild ▸ Clean Project; entities use public fields so this should not occur |
| App shows no recipes | Pantry is empty by design: Settings ▸ **Load demo pantry** |
| Web preview data missing after reopening | The browser blocks `localStorage` (private mode). The preview warns you and falls back to memory |
| `run_all.sh` cannot find `javac` | Install a JDK (not just a JRE) and make sure `javac -version` works |

## 9. Submission reminders (from the assignment PDF)

The PDF also requires things this package cannot produce for you: a **public GitHub repository with at least
10 genuine commits** made as you work (the history is checked against your video), a **5–7 minute narrated
video** (`docs/demo_script.md` is a plan), a **written report with real screenshots** of your running app
(`docs/system_design.md` has the diagrams), and a signed declaration of originality. You must be able to explain
and defend every part of the code you submit — read through `logic/RecipeMatcher.java` and the Activities until you can.

Naming for the ZIP: `Studentnumber_Surname_MobileAppDev700_Assignment.zip` (≤ 50 MB; exclude `build/` and `.gradle/`).
