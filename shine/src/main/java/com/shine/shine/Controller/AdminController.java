package com.shine.shine.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity; // Make sure you have a UserService
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shine.shine.Service.UserService;
import com.shine.shine.dto.UserDto;

@RestController
@RequestMapping("/api/admin") // All endpoints here will start with /api/admin
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint to get a list of all users
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAllUsersAsDto();
        return ResponseEntity.ok(users);
    }

    // Endpoint to delete a user by their ID
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        boolean deleted = userService.deleteUserById(userId);
        if (deleted) {
            return ResponseEntity.noContent().build(); // Standard response for successful delete
        } else {
            return ResponseEntity.notFound().build(); // If the user didn't exist
        }
    }
}