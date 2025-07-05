// File: src/main/java/com/shine/shine/dto/UpdateQuantityRequestDto.java
package com.shine.shine.dto;

public class UpdateQuantityRequestDto {
    private Integer cartItemId;
    private Integer quantity;

    // Default constructor
    public UpdateQuantityRequestDto() {}

    // Constructor with parameters
    public UpdateQuantityRequestDto(Integer cartItemId, Integer quantity) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Integer getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Integer cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "UpdateQuantityRequestDto{" +
                "cartItemId=" + cartItemId +
                ", quantity=" + quantity +
                '}';
    }
}