package com.shine.shine.dto;

public class AddToCartRequestDto {
    private Integer variationId;
    private Integer quantity;

    // Default constructor
    public AddToCartRequestDto() {}

    // Constructor with parameters
    public AddToCartRequestDto(Integer variationId, Integer quantity) {
        this.variationId = variationId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Integer getVariationId() {
        return variationId;
    }

    public void setVariationId(Integer variationId) {
        this.variationId = variationId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "AddToCartRequestDto{" +
                "variationId=" + variationId +
                ", quantity=" + quantity +
                '}';
    }
} 