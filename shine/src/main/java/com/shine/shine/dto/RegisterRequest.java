package com.shine.shine.dto;

public class RegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    // Add Getters and Setters for all four fields...
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}