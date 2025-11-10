package com.ismt.foodbuddy.util;

/**
 * Utility class for validating user input data such as usernames, passwords, and email addresses.
 *
 * @author subashtharu
 */
public class InputDataValidator {

    public static boolean validateUsername(String username) {
        // Username must be between 3 and 20 characters and contain only alphanumeric characters
        return username != null && username.matches("^[a-zA-Z0-9]{3,20}$");
    }

    public static boolean validatePassword(String password) {
        // Password must be at least 8 characters long and contain at least one digit, one uppercase letter, and one lowercase letter
        return password != null && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
    }

    public static boolean validateEmail(String email) {
        // Simple email validation pattern
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

}
