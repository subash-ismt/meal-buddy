package com.ismt.foodbuddy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ismt.foodbuddy.R;
import com.ismt.foodbuddy.model.Plan;

import java.text.DateFormat;
import java.util.List;
import java.util.Date;

/**
 * Adapter to display planned recipes in ProfileActivity.
 * - Shows name, instructions and date
 * - Supports an optional delete button callback to allow removing items from DB
 */
public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

    public interface OnDeleteClickListener {
        void onDelete(Plan plan);
    }

    private final List<Plan> items;
    private OnDeleteClickListener deleteClickListener;

    public PlanAdapter(List<Plan> items) {
        this.items = items;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    @NonNull
    @Override
    public PlanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanAdapter.ViewHolder holder, int position) {
        Plan p = items.get(position);
        holder.name.setText(p.getRecipeName());
        holder.instructions.setText(p.getInstructions());
        holder.date.setText(DateFormat.getDateTimeInstance().format(new Date(p.getCreatedAt())));

        holder.deleteButton.setOnClickListener(v -> {
            if (deleteClickListener != null) deleteClickListener.onDelete(p);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Return the Plan item at the given adapter position or null if out of range.
     */
    public Plan getItem(int position) {
        if (position >= 0 && position < items.size()) return items.get(position);
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView instructions;
        TextView date;
        ImageButton deleteButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textPlanName);
            instructions = itemView.findViewById(R.id.textPlanInstructions);
            date = itemView.findViewById(R.id.textPlanDate);
            // Use runtime lookup for the delete button id to avoid generated-R mismatch in this environment
            int deleteId = itemView.getResources().getIdentifier("button_delete_plan", "id", itemView.getContext().getPackageName());
            deleteButton = (ImageButton) (deleteId != 0 ? itemView.findViewById(deleteId) : null);
            if (deleteButton == null) {
                // Fallback: create a dummy invisible button to avoid NPEs when resource isn't available
                deleteButton = new ImageButton(itemView.getContext());
                deleteButton.setVisibility(View.GONE);
            }
        }
    }
}
