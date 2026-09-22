# Requirements traceability (PDF → implementation)

Source: *Mobile App Development 700 – Practical Assignment: Smart Pantry Manager*.

| PDF ref | Requirement | Where implemented (Android) | Web preview equivalent |
|---|---|---|---|
| 2.2 | Add / edit / delete pantry items (name, quantity, unit, optional expiry) | `ui/AddEditIngredientActivity`, `data/PantryDao`, `PantryListActivity.onDelete` | Add/Edit screen, delete dialog + undo |
| 2.2 | Pantry list screen with RecyclerView bound to the database | `ui/PantryListActivity`, `adapter/PantryAdapter`, `res/layout/item_pantry.xml` | Pantry tab |
| 2.2 | Recipe collection: ≥15–20 recipes, seeded on first run, name + ingredients + steps | `data/SeedData` (20 recipes), `DatabaseSeeder.ensureRecipesSeeded`, `Recipe`, `RecipeIngredient` | `data/recipes.json` written to storage on first run |
| 2.2 | “Suggested Recipes” screen running strict matching | `ui/SuggestedRecipesActivity` → `logic/RecipeMatcher.evaluate` | Cook tab |
| 2.2 | Recipe detail: full ingredient list + method | `ui/RecipeDetailActivity`, `RecipeIngredientAdapter` | Recipe detail |
| 2.2 | Settings / profile screen | `ui/SettingsActivity`, `util/SettingsManager` | Settings tab |
| 2.2 | Feedback when zero recipes match | `strings.xml: no_match_*`, `SuggestedRecipesActivity.showSelectedTab` | “No recipes match your pantry yet” empty state |
| 2.3 | Strict rule: every ingredient present in at least the required quantity | `RecipeMatcher.evaluate` (`isReady()` only when `missingCount()==0`) | `Engine.evaluate` |
| 2.3 | 4 of 5 ingredients ⇒ NOT suggested | `RecipeMatcherTest.fiveIngredientsWithFourInPantryIsNotSuggested` | UI test “cheese recipes vanish” |
| 2.3 | Partial / almost recipes excluded from main list | Only `isReady()` goes to the ready list | Ready list vs Almost tab |
| 2.3 | Optional “Almost there” list, clearly separated | Second tab in `SuggestedRecipesActivity` | “Almost there” tab with notice |
| 2.3 | Robust to unit differences and singular/plural | `IngredientNormalizer`, `UnitConverter`; tests `singularPluralAndSynonymsMatch`, `unitsAreConverted` | same logic in `engine.js` |
| 2.3 / 3.3 | No maps, mapping SDK, GPS or location | No permissions in `AndroidManifest.xml`; no such dependencies in `app/build.gradle` | none used |
| 3.1 | Built in Java (not Kotlin) with Android Studio | All sources are `.java`; `app/build.gradle` has no Kotlin plugin | n/a |
| 3.1 | ≥4 screens/Activities | 5 Activities: Pantry, Add/Edit, Suggested, Detail, Settings | 5 screens |
| 3.1 | Intents to navigate and pass data | `EXTRA_ITEM_ID`, `EXTRA_RECIPE_ID`, `EXTRA_PREFILL_*`, bottom-nav Intents in `BaseActivity` | n/a |
| 3.1 | RecyclerView + custom Adapter with database data | `PantryAdapter`, `RecipeAdapter`, `RecipeIngredientAdapter` | n/a |
| 3.1 | Working navigation element | `BottomNavigationView` (`menu/bottom_nav.xml`) | Bottom tab bar |
| 3.1 | Input validation | `AddEditIngredientActivity.validate()` | Inline errors on the form |
| 3.1 | Clear mobile layouts | ConstraintLayout / LinearLayout in `res/layout` | Phone-frame layout |
| 3.2 | One database option; full CRUD; data persists after restart | Room / SQLite: `AppDatabase`, `PantryDao` | `localStorage` behind Room-style DAOs |
| 4 | GitHub repo, ≥10 commits, README | README provided; **commits must be your own real history** | n/a |
| 5 | 5–7 min narrated video | `docs/demo_script.md` (plan only) | Demo guide panel |
| 6 | Written report with real screenshots | `docs/system_design.md` (diagrams only) | n/a |
