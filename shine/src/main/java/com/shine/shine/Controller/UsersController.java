package com.shine.shine.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shine.shine.Entity.Users;
import com.shine.shine.Service.UserServiceImpl;
import com.shine.shine.dto.UserDto;

@RestController
@RequestMapping("/api/users") 
public class UsersController {

    private final UserServiceImpl userService;

    public UsersController(UserServiceImpl usersService) {
        this.userService = usersService;
    }

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMyProfile() {
        // We'll add the logic to get the current user's details
        UserDto userDto = userService.getCurrentUserProfile();
        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        try {
            if (userService.userExistsByEmail(user.getEmail())) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            Users savedUser = userService.createUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable("id") Integer id, @RequestBody Users userDetails) {
        Users updatedUser = userService.updateUser(id, userDetails);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Integer id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-email")
    public ResponseEntity<Users> getUserByEmail(@RequestParam("email") String email) {
        Optional<Users> user = userService.getUserByEmail(email);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
