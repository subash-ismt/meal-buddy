package com.ismt.foodbuddy.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ismt.foodbuddy.adapter.PlanAdapter;
import com.ismt.foodbuddy.dao.DatabaseHelper;
import com.ismt.foodbuddy.databinding.ActivityProfileBinding;
import com.ismt.foodbuddy.model.Plan;

import java.util.List;

/**
 * ProfileActivity shows the user's planned recipes.
 * Features added:
 * - Loads plan items from SQLite using DatabaseHelper
 * - Allows removing plan items via a delete button in each item
 * - Allows swipe-to-delete (left or right) using ItemTouchHelper
 * - Well-commented to be easier to understand
 */
public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set toolbar as action bar and enable back navigation
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DatabaseHelper(this);

        // Setup RecyclerView
        binding.recyclerViewPlans.setLayoutManager(new LinearLayoutManager(this));

        // Load plan items
        loadPlans();

        // Attach swipe-to-delete helper
        attachSwipeToDelete();
    }

    /**
     * Load plans from database and set adapter. Also handle delete callbacks from adapter.
     */
    private void loadPlans() {
        List<Plan> plans = dbHelper.getAllPlans();
        PlanAdapter adapter = new PlanAdapter(plans);

        // When adapter reports an explicit delete button click, remove from DB and refresh
        adapter.setOnDeleteClickListener(plan -> {
            boolean removed = dbHelper.removePlan(plan.getId());
            if (removed) {
                // refresh list
                loadPlans();
            }
        });

        binding.recyclerViewPlans.setAdapter(adapter);
    }

    /**
     * Attach an ItemTouchHelper to allow swipe-to-delete on plan items.
     */
    private void attachSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        // We don't support move/reorder in this list
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        // Retrieve the adapter and its item, then delete from DB and refresh
                        RecyclerView.Adapter<?> adapter = binding.recyclerViewPlans.getAdapter();
                        if (adapter instanceof PlanAdapter) {
                            PlanAdapter planAdapter = (PlanAdapter) adapter;
                            Plan p = planAdapter.getItem(position);
                            if (p != null) {
                                dbHelper.removePlan(p.getId());
                            }
                        }
                        // Refresh the list after deletion
                        loadPlans();
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewPlans);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
