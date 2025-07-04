// File: src/main/java/com/glorycode24/shine/repositories/RoleRepository.java
package com.shine.shine.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shine.shine.Entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name); // Useful for finding roles like "ROLE_USER"
}