package com.ismt.foodbuddy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ismt.foodbuddy.R;
import com.ismt.foodbuddy.model.CheckList;

import java.util.List;

public class CheckItemsAdapter extends RecyclerView.Adapter<CheckItemsAdapter.ViewHolder> {

    private final Context context;
    private final List<CheckList> items;

    public CheckItemsAdapter(Context context, List<CheckList> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckItemsAdapter.ViewHolder holder, int position) {
        CheckList item = items.get(position);
        holder.itemName.setText(item.getItemName());
        holder.availableLocation.setText(item.getAvailableLocation());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;
        public TextView availableLocation;


        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.textPlanName);
            availableLocation = itemView.findViewById(R.id.textPlanInstructions);

        }
    }
}
