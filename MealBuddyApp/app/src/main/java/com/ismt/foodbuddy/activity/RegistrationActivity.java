package com.ismt.foodbuddy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ismt.foodbuddy.MainActivity;
import com.ismt.foodbuddy.R;
import com.ismt.foodbuddy.dao.DatabaseHelper;
import com.ismt.foodbuddy.dao.MyDBHelper;
import com.ismt.foodbuddy.util.InputDataValidator;

/**
 * RegistrationActivity allows a user to register with email and password.
 * Data is validated and stored into SQLite using DatabaseHelper.
 * Note: Passwords are stored in plain text in this demo DB (not secure for production).
 */
public class RegistrationActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private Button registerButton;
    private CheckBox registerAdminCheckbox;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        dbHelper = new DatabaseHelper(this);

        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        registerButton = findViewById(R.id.register_button);
        registerAdminCheckbox = findViewById(R.id.register_admin_checkbox);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString();
                String confirmPassword = confirmPasswordInput.getText().toString();

                // Basic validation with user-friendly messages
                if (email.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegistrationActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!InputDataValidator.validateEmail(email)){
                    Toast.makeText(RegistrationActivity.this, "Invalid email format. Ex: example@gmail.com", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!InputDataValidator.validatePassword(password)){
                    Toast.makeText(RegistrationActivity.this, "Password must be at least 8 characters long and contain at least one digit, one uppercase letter, and one lowercase letter", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check duplicate email
                if (dbHelper.isEmailExists(email)) {
                    Toast.makeText(RegistrationActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isAdmin = registerAdminCheckbox != null && registerAdminCheckbox.isChecked();

                // Persist the user in SQLite
                boolean inserted = dbHelper.addUser(email, password, isAdmin);
                if (inserted) {
                    Toast.makeText(RegistrationActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    // clear fields
                    emailInput.setText("");
                    passwordInput.setText("");
                    confirmPasswordInput.setText("");

                    // navigate to the login screen (MainActivity)
                    Intent loginIntent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(loginIntent);
                } else {
                    Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
