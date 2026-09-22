package com.smartpantry.logic;

/**
 * Read-only view of one pantry entry, as needed by the matching engine.
 * The Room entity PantryItem implements this, which keeps the engine free of Android
 * dependencies so it can be unit-tested with plain Java.
 */
public interface Stock {
    long getId();
    String getName();
    double getQuantity();
    String getUnit();
    /** Expiry date as ISO text "yyyy-MM-dd", or null when the item has no expiry date. */
    String getExpiryDate();
}
