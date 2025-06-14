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
@Table(name = "order_items")
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderItemID")
    private Integer orderItemId;

    @ManyToOne
    @JoinColumn(name = "OrderID", nullable = false) 
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "VariationID", nullable = false) 
    private ProductVariations productVariation;

    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

    @Column(name = "PricePerUnit", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerUnit;

    public OrderItems() {}

    public OrderItems(Orders order, ProductVariations productVariation, Integer quantity, BigDecimal pricePerUnit) {
        this.order = order;
        this.productVariation = productVariation;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public ProductVariations getProductVariation() {
        return productVariation;
    }

    public void setProductVariation(ProductVariations productVariation) {
        this.productVariation = productVariation;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public String toString() {
        return "OrderItems{" +
                "orderItemId=" + orderItemId +
                ", orderId=" + (order != null ? order.getOrderId() : "null") +
                ", productVariationId=" + (productVariation != null ? productVariation.getVariationId() : "null") +
                ", quantity=" + quantity +
                ", pricePerUnit=" + pricePerUnit +
                '}';
    }
}