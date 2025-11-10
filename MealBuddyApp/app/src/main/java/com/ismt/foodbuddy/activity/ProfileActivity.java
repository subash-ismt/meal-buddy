package com.ismt.foodbuddy.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ismt.foodbuddy.adapter.PlanAdapter;
import com.ismt.foodbuddy.dao.DatabaseHelper;
import com.ismt.foodbuddy.databinding.ActivityProfileBinding;
import com.ismt.foodbuddy.model.Plan;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DatabaseHelper(this);

        binding.recyclerViewPlans.setLayoutManager(new LinearLayoutManager(this));
        loadPlans();
    }

    private void loadPlans() {
        List<Plan> plans = dbHelper.getAllPlans();
        PlanAdapter adapter = new PlanAdapter(plans);
        binding.recyclerViewPlans.setAdapter(adapter);
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
