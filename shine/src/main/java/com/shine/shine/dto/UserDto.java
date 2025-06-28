package com.shine.shine.dto;

// This is a plain old Java object (POJO). It just holds data.
public class UserDto {

    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

    // --- Getters and Setters for all fields ---
    // These are required for the serialization library (Jackson) to work.

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}