// File: src/main/java/com/shine/shine/dto/CreateOrderRequestDto.java
package com.shine.shine.dto;

public class CreateOrderRequestDto {
    private Integer shippingAddressId;
    private Integer billingAddressId;

    // Add Getters and Setters
    public Integer getShippingAddressId() { return shippingAddressId; }
    public void setShippingAddressId(Integer shippingAddressId) { this.shippingAddressId = shippingAddressId; }
    public Integer getBillingAddressId() { return billingAddressId; }
    public void setBillingAddressId(Integer billingAddressId) { this.billingAddressId = billingAddressId; }
}