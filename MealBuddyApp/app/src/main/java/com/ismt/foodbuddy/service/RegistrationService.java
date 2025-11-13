package com.ismt.foodbuddy.service;

import com.ismt.foodbuddy.dao.DatabaseHelper;

/**
 * RegistrationService (simple placeholder) demonstrates how business logic could be
 * factored into a service layer. For now it contains simple validation helpers.
 * In a real app you might perform async registration, network calls, or additional
 * business rules here.
 */
public class RegistrationService {

    private DatabaseHelper dbHelper;

    public void registerUser(String username, String password) {
        // Intentionally left simple: actual registration is done in RegistrationActivity
        // using DatabaseHelper directly. This class can be expanded to perform async
        // registration, password hashing, or remote API calls.
    }


    public boolean validatePassword(String password) {

        // Keep logic small and consistent with InputDataValidator
        if (password == null) return false;
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
    }

    public boolean validateUsername(String username) {
        if (username == null) return false;
        return username.matches("^[a-zA-Z0-9]{3,20}$");
    }

}
