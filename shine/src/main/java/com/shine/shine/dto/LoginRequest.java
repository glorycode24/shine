package com.shine.shine.dto;

public class LoginRequest {

    private String email;
    private String password;

    // --- Getters and Setters ---
    // These are essential for Spring to be able to read the JSON data.

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}