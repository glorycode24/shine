package com.shine.shine.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;

import com.shine.shine.Entity.Users;
import com.shine.shine.Repository.UsersRepository;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Optional<Users> getUserById(Integer id) {
        return usersRepository.findById(id);
    }

    @Transactional
    public Users createUser(Users user) {
  
        if (user.getRegistrationDate() == null) {
            user.setRegistrationDate(LocalDateTime.now());
        }
        return usersRepository.save(user);
    }

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

    @Transactional
    public boolean deleteUser(Integer id) {
        if (usersRepository.existsById(id)) {
            usersRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Users> getUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public boolean userExistsByEmail(String email) {
        return usersRepository.existsByEmail(email);
    }
}