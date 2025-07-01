package com.shine.shine.Service;

import java.util.List;
import java.util.Optional;

import com.shine.shine.Entity.Users;
import com.shine.shine.dto.UserDto;

// This is the "contract".
public interface UserService {
    
    // Defines all the public methods that will be available.
    UserDto getCurrentUserProfile();
    List<Users> getAllUsers();
    Optional<Users> getUserById(Integer id);
    Users createUser(Users user);
    Users updateUser(Integer id, Users userDetails);
    boolean deleteUser(Integer id);
    Optional<Users> getUserByEmail(String email);
    boolean userExistsByEmail(String email);
    List<UserDto> findAllUsersAsDto();
    boolean deleteUserById(Integer userId);
}