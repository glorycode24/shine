package com.shine.shine.Repository;

import com.shine.shine.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email); // Add this method
    boolean existsByEmail(String email); // Useful for checking if email already exists during registration
}