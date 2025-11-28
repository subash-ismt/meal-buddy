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
import com.ismt.foodbuddy.model.User;
import com.ismt.learning.FirestoreService;
import com.ismt.foodbuddy.util.InputDataValidator;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private Button registerButton;
    private Button loginButton;
    private CheckBox registerAdminCheckbox;
    private FirestoreService firestoreService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firestoreService = new FirestoreService();

        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        registerButton = findViewById(R.id.register_button);
        registerAdminCheckbox = findViewById(R.id.register_admin_checkbox);
        loginButton = findViewById(R.id.material_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegistration();
            }
        });
    }

    private void handleRegistration() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        boolean isAdmin = registerAdminCheckbox != null && registerAdminCheckbox.isChecked();

        if (!validateInput(email, password, confirmPassword)) {
            return;
        }

        //interact with FirestoreService to register user
        firestoreService.registerUser(email, password, isAdmin, new FirestoreService.RegistrationCallback() {
            @Override
            public void onRegistrationSuccess() {
                Toast.makeText(RegistrationActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                clearInputFields();
                navigateToLogin();
            }

            @Override
            public void onRegistrationFailure(String errorMessage) {
                Toast.makeText(RegistrationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput(String email, String password, String confirmPassword) {
        if (email.isEmpty()) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!InputDataValidator.validateEmail(email)) {
            Toast.makeText(this, "Invalid email format. Ex: example@gmail.com", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!InputDataValidator.validatePassword(password)) {
            Toast.makeText(this, "Password must be at least 8 characters long and contain at least one digit, one uppercase letter, and one lowercase letter", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void clearInputFields() {
        emailInput.setText("");
        passwordInput.setText("");
        confirmPasswordInput.setText("");
    }

    private void navigateToLogin() {
        Intent loginIntent = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(loginIntent);
        finish(); // Prevent returning to registration screen
    }
}
