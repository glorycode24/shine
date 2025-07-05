package com.shine.shine.Service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shine.shine.Entity.ProductVariations;
import com.shine.shine.Repository.ProductVariationsRepository;

@Service
public class StockService {

    private final ProductVariationsRepository productVariationsRepository;

    public StockService(ProductVariationsRepository productVariationsRepository) {
        this.productVariationsRepository = productVariationsRepository;
    }

    /**
     * Check if there's sufficient stock for a product variation
     */
    public boolean hasSufficientStock(Integer variationId, Integer requestedQuantity) {
        System.out.println("DEBUG: Checking stock for variationId: " + variationId + ", requestedQuantity: " + requestedQuantity);
        
        Optional<ProductVariations> variation = productVariationsRepository.findById(variationId);
        if (variation.isEmpty()) {
            System.out.println("DEBUG: Variation not found for ID: " + variationId);
            return false;
        }
        
        ProductVariations productVariation = variation.get();
        Integer availableStock = productVariation.getAdditionalStock();
        System.out.println("DEBUG: Found variation - Product: " + productVariation.getProduct().getProductName() + 
                          ", Size: " + productVariation.getSize().getSizeName() + 
                          ", Color: " + productVariation.getColor().getColorName() + 
                          ", Available Stock: " + availableStock);
        
        boolean hasStock = availableStock >= requestedQuantity;
        System.out.println("DEBUG: Has sufficient stock: " + hasStock);
        return hasStock;
    }

    /**
     * Get available stock for a product variation
     */
    public Integer getAvailableStock(Integer variationId) {
        System.out.println("DEBUG: Getting available stock for variationId: " + variationId);
        
        Optional<ProductVariations> variation = productVariationsRepository.findById(variationId);
        if (variation.isEmpty()) {
            System.out.println("DEBUG: Variation not found for ID: " + variationId);
            return 0;
        }
        
        ProductVariations productVariation = variation.get();
        Integer availableStock = productVariation.getAdditionalStock();
        System.out.println("DEBUG: Available stock for variation " + variationId + ": " + availableStock);
        return availableStock;
    }

    /**
     * Reserve stock for a product variation (decrease stock)
     */
    @Transactional
    public boolean reserveStock(Integer variationId, Integer quantity) {
        Optional<ProductVariations> variation = productVariationsRepository.findById(variationId);
        if (variation.isEmpty()) {
            return false;
        }

        ProductVariations productVariation = variation.get();
        if (productVariation.getAdditionalStock() < quantity) {
            return false;
        }

        productVariation.setAdditionalStock(productVariation.getAdditionalStock() - quantity);
        productVariationsRepository.save(productVariation);
        return true;
    }

    /**
     * Release reserved stock (increase stock)
     */
    @Transactional
    public boolean releaseStock(Integer variationId, Integer quantity) {
        Optional<ProductVariations> variation = productVariationsRepository.findById(variationId);
        if (variation.isEmpty()) {
            return false;
        }

        ProductVariations productVariation = variation.get();
        productVariation.setAdditionalStock(productVariation.getAdditionalStock() + quantity);
        productVariationsRepository.save(productVariation);
        return true;
    }

    /**
     * Update stock quantity for a product variation
     */
    @Transactional
    public boolean updateStock(Integer variationId, Integer newQuantity) {
        Optional<ProductVariations> variation = productVariationsRepository.findById(variationId);
        if (variation.isEmpty()) {
            return false;
        }

        ProductVariations productVariation = variation.get();
        productVariation.setAdditionalStock(newQuantity);
        productVariationsRepository.save(productVariation);
        return true;
    }

    /**
     * Get product variation by ID
     */
    public Optional<ProductVariations> getProductVariation(Integer variationId) {
        return productVariationsRepository.findById(variationId);
    }
} 