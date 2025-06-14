package com.shine.shine.Controller;

import java.util.List;
import java.util.Optional; 

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shine.shine.Entity.ShoppingCarts;
import com.shine.shine.Service.ShoppingCartsService;

@RestController
@RequestMapping("/api/shopping-carts")
public class ShoppingCartsController {

    private final ShoppingCartsService shoppingCartService;

    public ShoppingCartsController(ShoppingCartsService shoppingCartService) { 
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public ResponseEntity<List<ShoppingCarts>> getAllShoppingCarts() {
        List<ShoppingCarts> carts = shoppingCartService.getAllShoppingCarts();
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoppingCarts> getShoppingCartById(@PathVariable("id") Integer id) {
        Optional<ShoppingCarts> cart = shoppingCartService.getShoppingCartById(id);
        return cart.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ShoppingCarts> createShoppingCart(@RequestBody ShoppingCarts shoppingCart) {
        try {
            ShoppingCarts createdCart = shoppingCartService.createShoppingCart(shoppingCart);
            return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            System.err.println("Error creating shopping cart: " + e.getMessage()); 
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShoppingCarts> updateShoppingCart(@PathVariable("id") Integer id, @RequestBody ShoppingCarts shoppingCartDetails) {
        try {
            ShoppingCarts updatedCart = shoppingCartService.updateShoppingCart(id, shoppingCartDetails);
            if (updatedCart != null) {
                return new ResponseEntity<>(updatedCart, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error updating shopping cart: " + e.getMessage()); 
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteShoppingCart(@PathVariable("id") Integer id) {
        boolean deleted = shoppingCartService.deleteShoppingCart(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<ShoppingCarts> getShoppingCartByUserId(@PathVariable("userId") Integer userId) {
        Optional<ShoppingCarts> cart = shoppingCartService.getShoppingCartByUserId(userId);
        return cart.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}