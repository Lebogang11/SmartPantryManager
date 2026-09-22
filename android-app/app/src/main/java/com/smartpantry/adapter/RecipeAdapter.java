package com.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartpantry.R;
import com.smartpantry.data.RecipeWithIngredients;
import com.smartpantry.logic.MatchResult;
import com.smartpantry.util.Formatters;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter for the Suggested Recipes screen. The same adapter renders both lists:
 * the strict "Ready to cook" list and the optional, clearly separate "Almost there" list.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    /** A recipe paired with the outcome of the strict-matching check. */
    public static class Row {
        public final RecipeWithIngredients recipe;
        public final MatchResult result;
        public Row(RecipeWithIngredients recipe, MatchResult result) { this.recipe = recipe; this.result = result; }
    }

    public interface Listener { void onRecipeClicked(Row row); }

    private final Listener listener;
    private List<Row> rows = new ArrayList<>();
    private boolean almostMode;

    public RecipeAdapter(Listener listener) { this.listener = listener; }

    public void submit(List<Row> newRows, boolean almostMode) {
        this.rows = new ArrayList<>(newRows);
        this.almostMode = almostMode;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        final Row row = rows.get(position);
        h.emoji.setText(row.recipe.recipe.emoji);
        h.name.setText(row.recipe.recipe.name);
        h.meta.setText(row.recipe.recipe.minutes + " min  •  serves " + row.recipe.recipe.servings
                + "  •  " + Formatters.plural(row.recipe.ingredients.size(), "ingredient"));

        StringBuilder chips = new StringBuilder();
        for (MatchResult.Line line : row.result.lines) {
            if (chips.length() > 0) chips.append("    ");
            chips.append(line.isOk() ? "✓ " : "＋ ").append(line.name);
        }
        h.ingredients.setText(chips);

        String extra = "";
        if (!almostMode && !row.result.expiringUsed.isEmpty()) {
            extra = "Uses up " + String.join(", ", row.result.expiringUsed) + " before it expires";
        } else if (almostMode) {
            MatchResult.Line problem = row.result.firstProblem();
            if (problem != null) {
                switch (problem.status) {
                    case SHORT: extra = "Short on " + problem.name + ": need " + problem.need + ", have " + problem.have; break;
                    case UNIT_MISMATCH: extra = "Can't compare units for " + problem.name + " (need " + problem.need + ")"; break;
                    default: extra = "Missing " + problem.name + " (" + problem.need + ")";
                }
            }
        }
        h.extra.setText(extra);
        h.extra.setVisibility(extra.isEmpty() ? View.GONE : View.VISIBLE);
        h.extra.setTextColor(h.itemView.getContext().getColor(almostMode ? R.color.bad_ink : R.color.warn_ink));

        h.itemView.setOnClickListener(v -> listener.onRecipeClicked(row));
    }

    @Override
    public int getItemCount() { return rows.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView emoji, name, meta, ingredients, extra;

        ViewHolder(@NonNull View v) {
            super(v);
            emoji = v.findViewById(R.id.textEmoji);
            name = v.findViewById(R.id.textRecipeName);
            meta = v.findViewById(R.id.textRecipeMeta);
            ingredients = v.findViewById(R.id.textIngredients);
            extra = v.findViewById(R.id.textExtra);
        }
    }
}
