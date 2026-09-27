package com.smartpantry.logic;

/**
 * Represents a pantry item used by the matching engine.
 * It provides the ingredient name, quantity and unit needed for matching.
 */
public interface Stock {
    long getId();
    String getName();
    double getQuantity();
    String getUnit();
    /** Expiry date as ISO text "yyyy-MM-dd", or null when the item has no expiry date. */
    String getExpiryDate();
}
