package com.shine.shine.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shine.shine.Entity.ProductVariations;

@Repository
public interface ProductVariationsRepository extends JpaRepository<ProductVariations, Integer> {
    List<ProductVariations> findByProduct_ProductId(Integer productId);

    Optional<ProductVariations> findByProduct_ProductIdAndSize_SizeIdAndColor_ColorId(
            Integer productId, Integer sizeId, Integer colorId);
}