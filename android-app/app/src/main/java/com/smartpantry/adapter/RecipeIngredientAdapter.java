package com.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartpantry.R;
import com.smartpantry.logic.MatchResult;
import com.smartpantry.util.Formatters;

import java.util.ArrayList;
import java.util.List;

/** Shows each ingredient of one recipe with a tick (in pantry) or cross (missing / not enough). */
public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.ViewHolder> {

    public interface Listener { void onAddMissing(int position); }

    private final Listener listener;
    private List<MatchResult.Line> lines = new ArrayList<>();

    public RecipeIngredientAdapter(Listener listener) { this.listener = listener; }

    public void submit(List<MatchResult.Line> newLines) {
        lines = new ArrayList<>(newLines);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        MatchResult.Line line = lines.get(position);
        h.name.setText(Formatters.capitalise(line.name));
        h.need.setText(line.need);
        h.mark.setImageResource(line.isOk() ? R.drawable.ic_check : R.drawable.ic_close);
        h.mark.setBackgroundResource(line.isOk() ? R.drawable.bg_mark_ok : R.drawable.bg_mark_bad);

        String detail;
        switch (line.status) {
            case OK: detail = "You have " + line.have; break;
            case SHORT: detail = "Not enough - you have " + line.have; break;
            case UNIT_MISMATCH: detail = "You have " + line.have + " (units can't be compared)"; break;
            default: detail = "Not in your pantry";
        }
        h.detail.setText(detail);
        h.detail.setTextColor(h.itemView.getContext().getColor(line.isOk() ? R.color.muted : R.color.bad_ink));

        h.add.setVisibility(line.isOk() ? View.GONE : View.VISIBLE);
        h.add.setOnClickListener(v -> listener.onAddMissing(h.getBindingAdapterPosition()));
    }

    @Override
    public int getItemCount() { return lines.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView mark;
        final TextView name, detail, need;
        final Button add;

        ViewHolder(@NonNull View v) {
            super(v);
            mark = v.findViewById(R.id.imageMark);
            name = v.findViewById(R.id.textIngName);
            detail = v.findViewById(R.id.textIngDetail);
            need = v.findViewById(R.id.textIngNeed);
            add = v.findViewById(R.id.buttonAddMissing);
        }
    }
}
