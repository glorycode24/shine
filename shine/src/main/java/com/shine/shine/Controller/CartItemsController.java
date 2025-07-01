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

import com.shine.shine.Entity.CartItems;
import com.shine.shine.Service.CartItemsService;

@RestController
@RequestMapping("/api/cart_items")
public class CartItemsController {

    private final CartItemsService cartItemService;

    public CartItemsController(CartItemsService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItems>> getAllCartItems() {
        List<CartItems> cartItems = cartItemService.getAllCartItems();
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItems> getCartItemById(@PathVariable("id") Integer id) {
        Optional<CartItems> cartItem = cartItemService.getCartItemById(id);
        return cartItem.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                       .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<CartItems> createCartItem(@RequestBody CartItems cartItem) {
        try {
            CartItems createdCartItem = cartItemService.createCartItem(cartItem);
            return new ResponseEntity<>(createdCartItem, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItems> updateCartItem(@PathVariable("id") Integer id, @RequestBody CartItems cartItemDetails) {
        try {
            CartItems updatedCartItem = cartItemService.updateCartItem(id, cartItemDetails);
            if (updatedCartItem != null) { 
                return new ResponseEntity<>(updatedCartItem, HttpStatus.OK);
            } else { 
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCartItem(@PathVariable("id") Integer id) {
        boolean deleted = cartItemService.deleteCartItem(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-shopping-cart/{cartId}")
    public ResponseEntity<List<CartItems>> getCartItemsByShoppingCartId(@PathVariable("cartId") Integer cartId) {
        List<CartItems> cartItems = cartItemService.getCartItemsByShoppingCartId(cartId);
        if (!cartItems.isEmpty()) {
            return new ResponseEntity<>(cartItems, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}