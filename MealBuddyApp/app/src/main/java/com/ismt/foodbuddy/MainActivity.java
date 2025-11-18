package com.ismt.foodbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ismt.foodbuddy.activity.RecipeListActivity;
import com.ismt.foodbuddy.activity.RegistrationActivity;
import com.ismt.foodbuddy.dao.DatabaseHelper;

/**
 * This is Android Activity class that is responsible for handling the login functionality.
 *
 * @author subashtharu
 */
public class MainActivity extends AppCompatActivity {

    // variables for UI elements for login form
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private TextView createAccountLink;
    private CheckBox adminCheckbox;

    private DatabaseHelper databaseHelper;

    // SharedPreferences keys
    public static final String PREFS_NAME = "mealbuddy_prefs";
    public static final String KEY_EMAIL = "pref_email";
    public static final String KEY_IS_ADMIN = "pref_is_admin";

    /**
     * This method is called when the activity is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);
        // Initialize UI elements for login form
        //findViewById method is used to find the UI element by its ID
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        createAccountLink = findViewById(R.id.create_account_link);
        adminCheckbox = findViewById(R.id.admin_checkbox);

        // If there's already a logged-in user in prefs, skip login
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedEmail = prefs.getString(KEY_EMAIL, null);
        if (savedEmail != null) {
            boolean savedAdmin = prefs.getBoolean(KEY_IS_ADMIN, false);
            Intent recipeListInt = new Intent(MainActivity.this, RecipeListActivity.class);
            recipeListInt.putExtra("is_admin", savedAdmin);
            startActivity(recipeListInt);
            finish();
            return;
        }

        //creating action on the button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString();

                if (!username.isEmpty() && !password.isEmpty()) {

                    //validate credentials
                    boolean valid = databaseHelper.validateUser(username, password);
                    if (valid) {
                        boolean wantsAdmin = adminCheckbox.isChecked();
                        if (wantsAdmin) {
                            // verify user actually has admin flag
                            boolean isAdmin = databaseHelper.isUserAdmin(username, password);
                            if (!isAdmin) {
                                Toast.makeText(MainActivity.this, "This account is not an admin", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        // Save session to SharedPreferences
                        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(KEY_EMAIL, username);
                        editor.putBoolean(KEY_IS_ADMIN, wantsAdmin);
                        editor.apply();

                        //Toast to pop up message
                        Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                        //Initialize intent to navigate to RecipeListActivity
                        Intent recipeListInt = new Intent(MainActivity.this, RecipeListActivity.class);
                        recipeListInt.putExtra("is_admin", wantsAdmin);
                        startActivity(recipeListInt);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //creating action on the link click
        createAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating intent to navigate to registration activity

                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent); //this method is used to start the activity defined form the intent object
            }
        });
    }
}
