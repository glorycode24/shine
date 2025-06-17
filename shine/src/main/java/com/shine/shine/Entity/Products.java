package com.shine.shine.Entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn; 
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table; 

@Entity
@Table(name = "products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductID")
    private Integer productId;

    @Column(name = "ProductName", nullable = false)
    private String productName;

    @Column(name = "Description", columnDefinition = "TEXT") 
    private String description;

    @Column(name = "SKU", unique = true) 
    private String sku;

    @Column(name = "Price", nullable = false, precision = 10, scale = 2) 
    private BigDecimal price;

    @Column(name = "ImageURL", length = 1000, nullable = true)
    private String imageUrl;

    @Column(name = "StockQuantity", nullable = false)
    private Integer stockQuantity;

    @ManyToOne
    @JoinColumn(name = "CategoryID", nullable = false) 
    private Categories category; 

    public Products() {}

    public Products(String productName, String description, String sku, BigDecimal price, String imageUrl, Integer stockQuantity, Categories category) {
        this.productName = productName;
        this.description = description;
        this.sku = sku;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
        this.category = category;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Products{" +
               "productId=" + productId +
               ", productName='" + productName + '\'' +
               ", description='" + description + '\'' +
               ", sku='" + sku + '\'' +
               ", price=" + price +
               ", imageUrl='" + imageUrl + '\'' +
               ", stockQuantity=" + stockQuantity +
               ", category=" + (category != null ? category.getCategoryName() : "null") +
               '}';
    }
}