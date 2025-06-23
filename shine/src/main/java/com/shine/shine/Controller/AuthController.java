package com.shine.shine.Controller; // Or com.shine.shine.controller

// 👇 ALL THE MISSING IMPORTS ARE ADDED HERE 👇
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shine.shine.Service.AuthService;
import com.shine.shine.Service.JwtService;
import com.shine.shine.dto.AuthResponse;
import com.shine.shine.dto.LoginRequest;
import com.shine.shine.dto.RegisterRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // These fields are final because they won't change after being set.
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // 👇 THIS CONSTRUCTOR FIXES THE INITIALIZATION ERRORS 👇
    // It correctly accepts and assigns all the final fields.
    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // Your register endpoint is good
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            authService.registerNewUser(registerRequest);
            return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Your login endpoint, now with all types correctly resolved
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String jwt = jwtService.generateToken(userDetails);

            // Return the token in the AuthResponse object
            return ResponseEntity.ok(new AuthResponse(jwt));

        } catch (Exception e) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
    }
}