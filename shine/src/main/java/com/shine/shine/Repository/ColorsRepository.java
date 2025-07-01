package com.shine.shine.Repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shine.shine.Entity.Colors;

public interface ColorsRepository extends JpaRepository<Colors, Integer>{
	Optional<Colors> findByColorName(String colorName);
}

