package com.shine.shine.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "shopping_carts")
public class ShoppingCarts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartID")
    private Integer cartId;

    @OneToOne
    @JoinColumn(name = "UserID", nullable = false, unique = true) 
    private Users user;

    @Column(name = "CreationDate", nullable = false)
    private LocalDateTime creationDate;

    public ShoppingCarts() {}

    public ShoppingCarts(Users user, LocalDateTime creationDate) {
        this.user = user;
        this.creationDate = creationDate;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "ShoppingCarts{" +
                "cartId=" + cartId +
                ", userId=" + (user != null ? user.getUserId() : "null") +
                ", creationDate=" + creationDate +
                '}';
    }


}