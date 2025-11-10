package com.ismt.foodbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    private DatabaseHelper databaseHelper;
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

        //creating action on the button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                if (!username.isEmpty() && !password.isEmpty()) {

                    //check in the database if the user exist??
                    boolean isAccountPresent = databaseHelper.isEmailExists(username);
                    if (isAccountPresent) {
                        //Toast to pop up message
                        Toast.makeText(MainActivity.this, "Account exists", Toast.LENGTH_SHORT).show();

                        //Initialize intent to navigate to RecipeListActivity
                        Intent recipeListInt = new Intent(MainActivity.this, RecipeListActivity.class);
                        startActivity(recipeListInt);
                    } else {
                        Toast.makeText(MainActivity.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                  //  Intent recipeListInt = new Intent(MainActivity.this, CombinedFragment.class);

                  //  startActivity(recipeListInt);
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
