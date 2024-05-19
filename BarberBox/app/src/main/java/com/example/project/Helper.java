package com.example.project;

public class Helper {
    public static String getUsername(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex != -1) {
            return email.substring(0, atIndex);
        } else {
            return "user";
        }
    }
}
