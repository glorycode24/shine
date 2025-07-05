package com.shine.shine.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProductWithVariationsRequest {
    private String productName;
    private String description;
    private BigDecimal basePrice;
    private Integer categoryId;
    private String imageUrl;
    private List<VariationDto> variations;

    // Getters and setters

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<VariationDto> getVariations() { return variations; }
    public void setVariations(List<VariationDto> variations) { this.variations = variations; }

    public static class VariationDto {
        private String color;
        private String size;
        private Integer stockQuantity;
        private BigDecimal price; // Optional

        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }

        public String getSize() { return size; }
        public void setSize(String size) { this.size = size; }

        public Integer getStockQuantity() { return stockQuantity; }
        public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
    }
}