package com.smartpantry.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.smartpantry.logic.Stock;

/**
 * One ingredient the user has at home. Room maps this class to the pantry_items table.
 * Implements {@link Stock} so the matching engine can read it without knowing about Room.
 */
@Entity(tableName = "pantry_items")
public class PantryItem implements Stock {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String name = "";

    public double quantity;

    @NonNull
    public String unit = "pcs";

    /** ISO date "yyyy-MM-dd", or null when the user did not enter an expiry date. */
    @Nullable
    public String expiryDate;

    /** Epoch millis when the row was created (used by the "Recently added" sort). */
    public long addedAt;

    /** Required by Room. */
    public PantryItem() { }

    @Ignore
    public PantryItem(@NonNull String name, double quantity, @NonNull String unit, @Nullable String expiryDate) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
        this.addedAt = System.currentTimeMillis();
    }

    @Override public long getId() { return id; }
    @Override public String getName() { return name; }
    @Override public double getQuantity() { return quantity; }
    @Override public String getUnit() { return unit; }
    @Override public String getExpiryDate() { return expiryDate; }
}
