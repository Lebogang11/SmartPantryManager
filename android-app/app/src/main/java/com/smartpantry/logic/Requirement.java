package com.smartpantry.logic;

/** One ingredient line a recipe needs (e.g. 200 g pasta). Implemented by the RecipeIngredient entity. */
public interface Requirement {
    String getName();
    double getQuantity();
    String getUnit();
}
