package com.ismt.foodbuddy.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ismt.foodbuddy.R;
import com.ismt.foodbuddy.dao.DatabaseHelper;

/**
 * Simple admin screen to add a recipe (name, instructions, ingredients) to the database.
 */
public class AddRecipeActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText instructionsInput;
    private EditText ingredientsInput;
    private Button saveButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        nameInput = findViewById(R.id.inputRecipeName);
        instructionsInput = findViewById(R.id.inputRecipeInstructions);
        ingredientsInput = findViewById(R.id.inputRecipeIngredients);
        saveButton = findViewById(R.id.buttonSaveRecipe);

        dbHelper = new DatabaseHelper(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString().trim();
                String instructions = instructionsInput.getText().toString().trim();
                String ingredients = ingredientsInput.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    nameInput.setError("Name required");
                    return;
                }

                boolean ok = dbHelper.addRecipe(name, instructions, ingredients);
                if (ok) {
                    Toast.makeText(AddRecipeActivity.this, "Recipe saved", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddRecipeActivity.this, "Failed to save recipe", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

