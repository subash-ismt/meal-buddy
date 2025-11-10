package com.ismt.foodbuddy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ismt.foodbuddy.R;
import com.ismt.foodbuddy.model.Plan;

import java.text.DateFormat;
import java.util.List;
import java.util.Date;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

    private final List<Plan> items;

    public PlanAdapter(List<Plan> items) {
        this.items = items;
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
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView instructions;
        TextView date;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textPlanName);
            instructions = itemView.findViewById(R.id.textPlanInstructions);
            date = itemView.findViewById(R.id.textPlanDate);
        }
    }
}
