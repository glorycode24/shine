package com.shine.shine.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shine.shine.Entity.ShoppingCarts;

@Repository
public interface ShoppingCartsRepository extends JpaRepository<ShoppingCarts, Integer> {
    Optional<ShoppingCarts> findByUser_UserId(Integer userId);
}