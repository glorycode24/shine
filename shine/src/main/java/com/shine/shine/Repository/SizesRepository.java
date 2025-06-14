package com.shine.shine.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shine.shine.Entity.Sizes;

@Repository
public interface SizesRepository extends JpaRepository<Sizes, Integer> {
    Optional<Sizes> findBySizeName(String sizeName);
    boolean existsBySizeName(String sizeName);
}