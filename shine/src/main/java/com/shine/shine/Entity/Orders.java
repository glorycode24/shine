package com.shine.shine.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID")
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    @Column(name = "OrderDate", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "OrderStatus", nullable = false)
    private String orderStatus;

    @ManyToOne
    @JoinColumn(name = "ShippingAddressID", nullable = false) 
    private Addresses shippingAddress;

    @ManyToOne
    @JoinColumn(name = "BillingAddressID", nullable = false) 
    private Addresses billingAddress;

    @Column(name = "TotalAmount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    // --- Constructors ---
    public Orders() {}

    public Orders(Users user, LocalDateTime orderDate, String orderStatus, Addresses shippingAddress, Addresses billingAddress, BigDecimal totalAmount) {
        this.user = user;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.totalAmount = totalAmount;
    }

    // --- Getters and Setters ---
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Addresses getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Addresses shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Addresses getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Addresses billingAddress) {
        this.billingAddress = billingAddress;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "orderId=" + orderId +
                ", user=" + (user != null ? user.getEmail() : "null") +
                ", orderDate=" + orderDate +
                ", orderStatus='" + orderStatus + '\'' +
                ", shippingAddressId=" + (shippingAddress != null ? shippingAddress.getAddressId() : "null") +
                ", billingAddressId=" + (billingAddress != null ? billingAddress.getAddressId() : "null") +
                ", totalAmount=" + totalAmount +
                '}';
    }
}