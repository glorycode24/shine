// File: src/main/java/com/shine/shine/dto/AddItemRequestDto.java
package com.shine.shine.dto;

public class AddItemRequestDto {
    private Integer variationId;
    private int quantity;

    // Add Getters and Setters
    public Integer getVariationId() { return variationId; }
    public void setVariationId(Integer variationId) { this.variationId = variationId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}