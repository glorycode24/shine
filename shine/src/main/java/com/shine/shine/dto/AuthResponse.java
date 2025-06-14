package com.shine.shine.dto;

public class AuthResponse {
    private String jwt;
    private String message;
    private String username;

    public AuthResponse(String jwt, String message, String username) {
        this.jwt = jwt;
        this.message = message;
        this.username = username;
    }

    public String getJwt() { return jwt; }
    public void setJwt(String jwt) { this.jwt = jwt; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}