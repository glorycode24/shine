package com.shine.shine.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shine.shine.Entity.Categories;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Integer> {
   
    Optional<Categories> findByCategoryName(String categoryName);
    boolean existsByCategoryName(String categoryName);
}