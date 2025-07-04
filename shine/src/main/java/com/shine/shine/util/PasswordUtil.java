package com.shine.shine.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    public static String hashPassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }
    
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
    
    // Main method to generate hashes for testing
    public static void main(String[] args) {
        if (args.length > 0) {
            String password = args[0];
            String hashedPassword = hashPassword(password);
            System.out.println("Password: " + password);
            System.out.println("Hashed: " + hashedPassword);
        } else {
            // Default test passwords
            System.out.println("Admin password (admin123): " + hashPassword("admin123"));
            System.out.println("User password (user123): " + hashPassword("user123"));
        }
    }
} 