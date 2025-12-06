package com.ismt.foodbuddy.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ismt.foodbuddy.R;
import com.ismt.learning.OnSwipeTouchListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ToDoListAdaptor extends RecyclerView.Adapter<ToDoListAdaptor.TodDoListViewHolder> {

    private Context context;

    private List<String> todoItems;
    private Set<String> completedItems = new HashSet<>();

    public ToDoListAdaptor(List<String> todoItems, Context context) {
        this.todoItems = todoItems;
        this.context = context;
    }

    @NonNull
    @Override
    public TodDoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //to return view
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new TodDoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodDoListViewHolder holder, int position) {

        String todoItem = todoItems.get(position);
        holder.todoName.setText(todoItem);

        boolean isCompleted = completedItems.contains(todoItem);
        holder.todoCheckbox.setChecked(isCompleted);

        if (isCompleted) {
            holder.todoName.setPaintFlags(holder.todoName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.todoName.setPaintFlags(holder.todoName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.todoCheckbox.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                String currentItem = todoItems.get(currentPosition);
                if (completedItems.contains(currentItem)) {
                    completedItems.remove(currentItem);
                } else {
                    completedItems.add(currentItem);
                }
                notifyItemChanged(currentPosition);
            }
        });

        holder.itemView.setOnTouchListener(new OnSwipeTouchListener(context) {
            @Override
            public void onSwipeLeft() {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    todoItems.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                    notifyItemRangeChanged(currentPosition, todoItems.size());
                    Toast.makeText(context, "Left Swipe: Item Deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSwipeRight() {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    String currentItem = todoItems.get(currentPosition);
                    if (completedItems.contains(currentItem)) {
                        completedItems.remove(currentItem);
                        Toast.makeText(context, "Item marked as not completed", Toast.LENGTH_SHORT).show();
                    } else {
                        completedItems.add(currentItem);
                        Toast.makeText(context, "Right Swipe: Item Completed", Toast.LENGTH_SHORT).show();
                    }
                    notifyItemChanged(currentPosition);
                }
            }

            @Override
            public void onSwipeBottom() {
                // You can add behavior for bottom swipes here
            }

            @Override
            public void onSwipeTop() {
                // You can add behavior for top swipes here
            }
        });

    }

    public void resetToDos() {
        completedItems.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    public static class TodDoListViewHolder extends RecyclerView.ViewHolder {
        TextView todoName;
        CheckBox todoCheckbox;
        public TodDoListViewHolder(View view) {
            super(view);
            todoName = view.findViewById(R.id.todoItemText);
            todoCheckbox = view.findViewById(R.id.todoCheckbox);
        }
    }
}
