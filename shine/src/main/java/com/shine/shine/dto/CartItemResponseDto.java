package com.shine.shine.dto;

import java.math.BigDecimal;

public class CartItemResponseDto {
    private Integer cartItemId;
    private Integer productId;
    private String productName;
    private String productImage;
    private BigDecimal productPrice;
    private Integer variationId;
    private String sizeName;
    private String colorName;
    private Integer quantity;
    private Integer availableStock;
    private BigDecimal totalPrice;

    // Default constructor
    public CartItemResponseDto() {}

    // Constructor with parameters
    public CartItemResponseDto(Integer cartItemId, Integer productId, String productName, 
                             String productImage, BigDecimal productPrice, Integer variationId,
                             String sizeName, String colorName, Integer quantity, 
                             Integer availableStock, BigDecimal totalPrice) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.variationId = variationId;
        this.sizeName = sizeName;
        this.colorName = colorName;
        this.quantity = quantity;
        this.availableStock = availableStock;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public Integer getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Integer cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getVariationId() {
        return variationId;
    }

    public void setVariationId(Integer variationId) {
        this.variationId = variationId;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "CartItemResponseDto{" +
                "cartItemId=" + cartItemId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productImage='" + productImage + '\'' +
                ", productPrice=" + productPrice +
                ", variationId=" + variationId +
                ", sizeName='" + sizeName + '\'' +
                ", colorName='" + colorName + '\'' +
                ", quantity=" + quantity +
                ", availableStock=" + availableStock +
                ", totalPrice=" + totalPrice +
                '}';
    }
} 