package com.ismt.foodbuddy.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import com.ismt.foodbuddy.MainActivity;
import com.ismt.foodbuddy.R;
import com.ismt.foodbuddy.adapter.RecipeAdapter;
import com.ismt.foodbuddy.dao.DatabaseHelper;
import com.ismt.foodbuddy.databinding.ActivityRecipeListActivyBinding;
import com.ismt.foodbuddy.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Activity that displays a list of recipes and allows adding them to the user's plan.
 * - Uses a RecyclerView with RecipeAdapter
 * - Persists planned recipes to SQLite via DatabaseHelper
 */
public class RecipeListActivity extends AppCompatActivity {

    private ActivityRecipeListActivyBinding binding;
    private RecipeAdapter adapter;
    private final List<Recipe> recipes = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRecipeListActivyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //this method sets the toolbar as the app bar for the activity
        setSupportActionBar(binding.toolbar);

        dbHelper = new DatabaseHelper(this);

        // read admin flag passed from login
        Intent i = getIntent();
        if (i != null) {
            isAdmin = i.getBooleanExtra("is_admin", false);
        }

        // if not passed via intent, read from shared prefs
        if (!isAdmin) {
            SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
            isAdmin = prefs.getBoolean(MainActivity.KEY_IS_ADMIN, false);
        }

        setupRecycler();
        loadDataFromDb();

        // show FAB only to admins
        if (binding.fab != null) {
            binding.fab.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        }

        binding.fab.setOnClickListener(view -> {
            // open admin AddRecipeActivity
            startActivity(new Intent(RecipeListActivity.this, AddRecipeActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // reload recipes in case admin added new one
        loadDataFromDb();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            // clear saved session
            SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(MainActivity.KEY_EMAIL);
            editor.remove(MainActivity.KEY_IS_ADMIN);
            editor.apply();

            // navigate back to login
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecycler() {
        adapter = new RecipeAdapter(recipes);
        adapter.setOnAddClickListener((recipe, position) -> {
            View root = binding.getRoot();
            // Prevent duplicate plan entries by recipe name
            if (dbHelper != null && dbHelper.isPlanExists(recipe.getName())) {
                Snackbar.make(root, "Recipe already added to your plan", Snackbar.LENGTH_SHORT).show();
                return;
            }

            boolean saved = false;
            if (dbHelper != null) {
                saved = dbHelper.addPlan(recipe.getName(), recipe.getInstructions());
            }

            if (saved) {
                Snackbar.make(root, "Added to plan: " + recipe.getName(), Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(root, "Failed to add to plan", Snackbar.LENGTH_SHORT).show();
            }
        });

        binding.recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewRecipes.setAdapter(adapter);
    }

    private void loadDataFromDb() {
        recipes.clear();
        if (dbHelper != null) {
            recipes.addAll(dbHelper.getAllRecipes());
        }
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}