# System design (for the report)

## Screen flow

```mermaid
flowchart LR
    subgraph Bottom navigation
      P[Pantry list<br/>PantryListActivity]
      C[Suggested recipes<br/>SuggestedRecipesActivity]
      S[Settings<br/>SettingsActivity]
    end
    P -- "FAB / empty-state button" --> A[Add / Edit ingredient<br/>AddEditIngredientActivity]
    P -- "tap item (EXTRA_ITEM_ID)" --> A
    C -- "tap recipe (EXTRA_RECIPE_ID)" --> D[Recipe detail<br/>RecipeDetailActivity]
    D -- "Add missing (EXTRA_PREFILL_*)" --> A
    D -- "Mark as cooked" --> C
    A -- "Save / Cancel" --> P
    P <--> C
    C <--> S
    P <--> S
```

## Data model (ER diagram)

```mermaid
erDiagram
    RECIPES ||--|{ RECIPE_INGREDIENTS : "has"
    RECIPES {
        int id PK
        text name
        text emoji
        int minutes
        int servings
        text steps
    }
    RECIPE_INGREDIENTS {
        int id PK
        int recipeId FK
        text name
        real quantity
        text unit
    }
    PANTRY_ITEMS {
        int id PK
        text name
        real quantity
        text unit
        text expiryDate
        int addedAt
    }
```
`PANTRY_ITEMS` and `RECIPE_INGREDIENTS` are related **by meaning, not by a key**: the matching engine links them by
comparing normalised ingredient names (`IngredientNormalizer`), which is what makes “Tomatoes” match “tomato”.

## Strict-matching algorithm (pseudo-code)

```
for each recipe:
    for each required ingredient r:
        key   = normalise(r.name)
        have  = sum of all usable pantry entries with normalise(name) == key, converted to r's unit family
        need  = r.quantity converted to base units
        if have < need:  mark r as NOT covered
    if no ingredient is NOT covered:   -> "Ready to cook"   (strict list)
    else if exactly one is NOT covered -> "Almost there"    (optional, separate list)
    else                               -> not shown
```
