package com.shine.shine.Service;

import com.shine.shine.Entity.Users;
import com.shine.shine.Repository.UsersRepository;
import com.shine.shine.dto.RegisterRequest; // We will create this DTO
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    // Spring automatically injects the repository and the PasswordEncoder bean we just created.
    public AuthService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users registerNewUser(RegisterRequest registerRequest) {
        // 1. Check if a user with this email already exists
        if (usersRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new IllegalStateException("An account with this email already exists.");
        }

        // 2. Create a new User entity object
        Users newUser = new Users();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setFirstName(registerRequest.getFirstName());
        newUser.setLastName(registerRequest.getLastName());
        
        // 3. THIS IS THE CRUCIAL STEP: Hash the password before saving!
        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
        newUser.setPasswordHash(hashedPassword); // Make sure this matches your entity's field name

        // 4. Set default values for the new user
        newUser.setRegistrationDate(LocalDateTime.now());
        newUser.setRole("USER"); // Assign a default role

        // 5. Save the fully prepared user to the database
        return usersRepository.save(newUser);
    }
}