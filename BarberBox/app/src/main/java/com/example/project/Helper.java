package com.example.project;

public class Helper {

    // Constant for a good password
    public static final String GOOD_PASSWORD = "GOOD PASSWORD";

    public static String checkPassword(String password) {

        // Check if the password is at least 8 characters long
        if (password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }

        // Check if the password contains at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter.";
        }

        // Check if the password contains at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter.";
        }

        // Check if the password contains at least one digit
        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit.\n";
        }

        return GOOD_PASSWORD;

    }

    public static String getUsername(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex != -1) {
            return email.substring(0, atIndex);
        } else {
            return "user";
        }
    }
}
