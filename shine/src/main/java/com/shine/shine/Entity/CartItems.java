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
@Table(name = "cart_items")
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartItemID")
    private Integer cartItemId;

    @ManyToOne
    @JoinColumn(name = "CartID", nullable = false)
    private ShoppingCarts shoppingCart;

    @ManyToOne
    @JoinColumn(name = "VariationID", nullable = false) 
    private ProductVariations productVariation;

    @ManyToOne // This defines the relationship: Many cart items can point to ONE product.
    @JoinColumn(name = "ProductID", nullable = false) // This specifies the foreign key column in this table.
    private Products product;

    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

    public CartItems() {}

    public CartItems(ShoppingCarts shoppingCart, ProductVariations productVariation, Integer quantity) {
        this.shoppingCart = shoppingCart;
        this.productVariation = productVariation;
        this.quantity = quantity;
    }

    public Integer getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Integer cartItemId) {
        this.cartItemId = cartItemId;
    }

    public ShoppingCarts getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCarts shoppingCart) {
        this.shoppingCart = shoppingCart;
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

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItems{" +
                "cartItemId=" + cartItemId +
                ", shoppingCartId=" + (shoppingCart != null ? shoppingCart.getCartId() : "null") +
                ", productVariationId=" + (productVariation != null ? productVariation.getVariationId() : "null") +
                ", quantity=" + quantity +
                '}';
    }
}