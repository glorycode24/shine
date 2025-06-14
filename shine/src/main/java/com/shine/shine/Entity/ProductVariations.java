package com.shine.shine.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_variations") 
public class ProductVariations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VariationID")
    private Integer variationId;

    @ManyToOne
    @JoinColumn(name = "ProductID", nullable = false) 
    private Products product;

    @ManyToOne
    @JoinColumn(name = "SizeID", nullable = false) 
    private Sizes size;

    @ManyToOne
    @JoinColumn(name = "ColorID", nullable = false) 
    private Colors color;

    @Column(name = "AdditionalStock", nullable = false)
    private Integer additionalStock;

    public ProductVariations() {}

    public ProductVariations(Products product, Sizes size, Colors color, Integer additionalStock) {
        this.product = product;
        this.size = size;
        this.color = color;
        this.additionalStock = additionalStock;
    }

    public Integer getVariationId() {
        return variationId;
    }

    public void setVariationId(Integer variationId) {
        this.variationId = variationId;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public Sizes getSize() {
        return size;
    }

    public void setSize(Sizes size) {
        this.size = size;
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public Integer getAdditionalStock() {
        return additionalStock;
    }

    public void setAdditionalStock(Integer additionalStock) {
        this.additionalStock = additionalStock;
    }

    @Override
    public String toString() {
        return "ProductVariations{" +
                "variationId=" + variationId +
                ", product=" + (product != null ? product.getProductName() : "null") +
                ", size=" + (size != null ? size.getSizeName() : "null") +
                ", color=" + (color != null ? color.getColorName() : "null") +
                ", additionalStock=" + additionalStock +
                '}';
    }
}