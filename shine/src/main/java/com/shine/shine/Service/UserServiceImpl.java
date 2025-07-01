package com.shine.shine.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;

import com.shine.shine.Entity.Users;
import com.shine.shine.Repository.UsersRepository;
import com.shine.shine.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {
    
    private final UsersRepository usersRepository;

    public UserServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    @Override
    public UserDto getCurrentUserProfile() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            throw new IllegalStateException("User is not authenticated");
        }
        Users user = usersRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return convertToDto(user);
    }
    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }
    @Override
    public Optional<Users> getUserById(Integer id) {
        return usersRepository.findById(id);
    }
    @Override
    @Transactional
    public Users createUser(Users user) {
        
        Optional<Users> existingUser = usersRepository.findByEmail(user.getEmail());
    if (existingUser.isPresent()) {
        throw new IllegalArgumentException("Email already registered: " + user.getEmail());
    }
        if (user.getRegistrationDate() == null) {
            user.setRegistrationDate(LocalDateTime.now());
        }
        return usersRepository.save(user);
    }
    @Override
    @Transactional
    public Users updateUser(Integer id, Users userDetails) {
        Optional<Users> optionalUser = usersRepository.findById(id);
        if (optionalUser.isPresent()) {
            Users existingUser = optionalUser.get();
            existingUser.setFirstName(userDetails.getFirstName());
            existingUser.setLastName(userDetails.getLastName());
            existingUser.setEmail(userDetails.getEmail());
         
            existingUser.setPasswordHash(userDetails.getPasswordHash());
            existingUser.setRegistrationDate(userDetails.getRegistrationDate()); 

            return usersRepository.save(existingUser);
        } else {
            return null;
        }
    }
    @Override
    @Transactional
    public boolean deleteUser(Integer id) {
        if (usersRepository.existsById(id)) {
            usersRepository.deleteById(id);
            return true;
        }
        return false;
    }
    @Override
    public Optional<Users> getUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }
    @Override
    public boolean userExistsByEmail(String email) {
        return usersRepository.existsByEmail(email);
    }

    private UserDto convertToDto(Users user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    @Override
    public List<UserDto> findAllUsersAsDto() {
        return usersRepository.findAll() // Fetch all User entities
                .stream()                 // Convert to a stream
                .map(this::convertToDto)  // Convert each User to a UserDto
                .collect(Collectors.toList()); // Collect the results into a list
    }

    @Override
    public boolean deleteUserById(Integer userId) {
        if (usersRepository.existsById(userId)) {
            usersRepository.deleteById(userId);
            return true;
        }
        return false;
    }
    
}