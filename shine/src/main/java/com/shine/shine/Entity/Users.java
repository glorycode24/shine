package com.shine.shine.Entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; // Import the UserDetails interface

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
// 👇 1. IMPLEMENT THE UserDetails INTERFACE 👇
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Integer userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "role")
    private String role;

    // --- Constructors --- (No changes needed here)
    public Users() {}

    public Users(String email, String firstName, String lastName, String passwordHash) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.registrationDate = LocalDateTime.now();
        this.role = "USER";
    }

    // --- Getters and Setters --- (No changes needed here)
    // ... all your existing getters and setters ...
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // 👇 2. ADD THE REQUIRED METHODS FROM THE UserDetails INTERFACE 👇
    // These methods provide the "ID card" for Spring Security.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // This converts your role String (e.g., "USER") into a format Spring Security understands.
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public String getPassword() {
        // CRUCIAL: This method MUST return the hashed password.
        return this.passwordHash;
    }

    @Override
    public String getUsername() {
        // CRUCIAL: Since you log in with email, this method MUST return the email.
        return this.email;
    }

    // For now, we can hardcode these to true.
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
    
    // ... your toString() method ...
}