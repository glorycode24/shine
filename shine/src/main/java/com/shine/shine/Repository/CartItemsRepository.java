package com.shine.shine.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shine.shine.Entity.CartItems;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Integer> {
    List<CartItems> findByShoppingCart_CartId(Integer cartId);

    Optional<CartItems> findByShoppingCart_CartIdAndProductVariation_VariationId(Integer cartId, Integer variationId);
}