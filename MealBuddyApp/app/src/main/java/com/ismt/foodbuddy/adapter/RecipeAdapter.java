package com.ismt.foodbuddy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ismt.foodbuddy.R;
import com.ismt.foodbuddy.model.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    public interface OnAddClickListener {
        void onAddClicked(Recipe recipe, int position);
    }

    private final List<Recipe> items;
    private OnAddClickListener addClickListener;

    public RecipeAdapter(List<Recipe> items) {
        this.items = items;
    }

    public void setOnAddClickListener(OnAddClickListener listener) {
        this.addClickListener = listener;
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        Recipe r = items.get(position);
        holder.name.setText(r.getName());
        holder.instructions.setText(r.getInstructions());
        holder.addButton.setOnClickListener(v -> {
            if (addClickListener != null) addClickListener.onAddClicked(r, holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView instructions;
        Button addButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textRecipeName);
            instructions = itemView.findViewById(R.id.textRecipeInstructions);
            addButton = itemView.findViewById(R.id.buttonAddPlan);
        }
    }
}
