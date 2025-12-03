package com.ismt.foodbuddy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ismt.foodbuddy.R;
import com.ismt.foodbuddy.model.Equipment;

import java.util.List;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {

    private final List<Equipment> equipments;

    public EquipmentAdapter(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_equipment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Equipment equipment = equipments.get(position);
        holder.equipmentName.setText(equipment.getName());
        holder.availableLocation.setText("Available at: " + equipment.getAvailableLocation());
        holder.price.setText("Price: $" + equipment.getPrice());
        holder.isPurchased.setText("Purchased: " + (equipment.isPurchased() ? "Yes" : "No"));

    }

    @Override
    public int getItemCount() {
        return equipments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView equipmentName, availableLocation, price, isPurchased;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            equipmentName = itemView.findViewById(R.id.textViewEquipmentName);
            availableLocation = itemView.findViewById(R.id.textViewAvailableLocation);
            price = itemView.findViewById(R.id.textViewPrice);
            isPurchased = itemView.findViewById(R.id.isPurchasedTextView);
        }
    }
}
