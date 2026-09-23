package com.smartpantry.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.smartpantry.R;
import com.smartpantry.data.PantryItem;
import com.smartpantry.logic.RecipeMatcher;
import com.smartpantry.util.Formatters;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom RecyclerView adapter for the pantry list. Each row shows the name, quantity and an
 * expiry tag coloured by status (fresh / expiring soon / expired), plus edit and delete buttons.
 */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    public interface Listener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    private final Listener listener;
    private List<PantryItem> items = new ArrayList<>();
    private LocalDate today = LocalDate.now();
    private int soonDays = 3;
    private boolean showExpiryTag = false;

    public PantryAdapter(Listener listener) { this.listener = listener; }

    public void submit(List<PantryItem> newItems, LocalDate today, int soonDays, boolean showExpiryTag) {
        this.items = new ArrayList<>(newItems);
        this.today = today;
        this.soonDays = soonDays;
        this.showExpiryTag = showExpiryTag;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        final PantryItem item = items.get(position);
        h.name.setText(Formatters.capitalise(item.name));
        h.quantity.setText(Formatters.quantity(item.quantity, item.unit));

        Integer days = RecipeMatcher.daysUntilExpiry(item.expiryDate, today);
        boolean needsAttention = showExpiryTag && days != null && days <= soonDays;
        if (needsAttention) {
            h.expiry.setVisibility(View.VISIBLE);
            h.expiry.setText(Formatters.expiryLabel(days));
            int bg = days < 0 ? R.color.bad_bg : R.color.warn_bg;
            int fg = days < 0 ? R.color.bad_ink : R.color.warn_ink;
            h.expiry.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(h.itemView.getContext(), bg)));
            h.expiry.setTextColor(ContextCompat.getColor(h.itemView.getContext(), fg));
        } else {
            h.expiry.setVisibility(View.GONE);
        }
        h.itemView.setOnClickListener(v -> listener.onEdit(item));
        h.edit.setOnClickListener(v -> listener.onEdit(item));
        h.delete.setOnClickListener(v -> listener.onDelete(item));
        h.edit.setContentDescription("Edit " + item.name);
        h.delete.setContentDescription("Delete " + item.name);
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name, quantity, expiry;
        final ImageButton edit, delete;

        ViewHolder(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.textName);
            quantity = v.findViewById(R.id.textQuantity);
            expiry = v.findViewById(R.id.textExpiry);
            edit = v.findViewById(R.id.buttonEdit);
            delete = v.findViewById(R.id.buttonDelete);
        }
    }
}
