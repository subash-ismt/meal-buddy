package com.ismt.foodbuddy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ismt.foodbuddy.R;
import com.ismt.foodbuddy.model.Workout;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {

    private final List<Workout> workouts;

    public WorkoutAdapter(List<Workout> workouts) {
        this.workouts = workouts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Workout workout = workouts.get(position);
        holder.workoutName.setText(workout.getName());
        holder.duration.setText("Duration: " + workout.getDuration() + " minutes");
        holder.difficulty.setText("Difficulty: " + workout.getDifficultyLevel());
        holder.muscleGroup.setText("Muscle Group: " + workout.getMuscleGroup());
        holder.equipmentNeeded.setText("Equipment: " + String.join(", ", workout.getEquipmentNeeded()));
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView workoutName, duration, difficulty, muscleGroup, equipmentNeeded, isPurchased;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutName = itemView.findViewById(R.id.textViewWorkoutName);
            duration = itemView.findViewById(R.id.textViewDuration);
            difficulty = itemView.findViewById(R.id.textViewDifficulty);
            muscleGroup = itemView.findViewById(R.id.textViewMuscleGroup);
            equipmentNeeded = itemView.findViewById(R.id.textViewEquipmentNeeded);
            isPurchased = itemView.findViewById(R.id.isPurchasedTextView);
        }
    }
}
