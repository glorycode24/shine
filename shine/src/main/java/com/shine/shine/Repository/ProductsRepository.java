package com.shine.shine.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shine.shine.Entity.Products;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer> {
 
}