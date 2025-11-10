package com.ismt.foodbuddy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import com.ismt.foodbuddy.R;
import com.ismt.foodbuddy.adapter.RecipeAdapter;
import com.ismt.foodbuddy.dao.DatabaseHelper;
import com.ismt.foodbuddy.databinding.ActivityRecipeListActivyBinding;
import com.ismt.foodbuddy.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

public class RecipeListActivity extends AppCompatActivity {

    private ActivityRecipeListActivyBinding binding;
    private RecipeAdapter adapter;
    private final List<Recipe> recipes = new ArrayList<>();
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRecipeListActivyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        dbHelper = new DatabaseHelper(this);

        setupRecycler();
        loadSampleData();

        binding.fab.setOnClickListener(view ->
                Snackbar.make(view, "Add new recipe - implement add flow", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show()
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecycler() {
        adapter = new RecipeAdapter(recipes);
        adapter.setOnAddClickListener((recipe, position) -> {
            View root = binding.getRoot();
            Snackbar.make(root, "Added to plan: " + recipe.getName(), Snackbar.LENGTH_SHORT).show();
            // persist plan entry
            if (dbHelper != null) {
                dbHelper.addPlan(recipe.getName(), recipe.getInstructions());
            }
        });

        binding.recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewRecipes.setAdapter(adapter);
    }

    private void loadSampleData() {
        recipes.add(new Recipe("Spicy Tomato Pasta", "Boil pasta. Sauté garlic and tomatoes. Mix with chili flakes and cheese."));
        recipes.add(new Recipe("Grilled Chicken Salad", "Marinate chicken, grill, slice. Toss with greens, dressing and nuts."));
        recipes.add(new Recipe("Vegetable Stir Fry", "Chop veggies, stir-fry with soy sauce and sesame oil. Serve with rice."));
        // notify adapter
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}