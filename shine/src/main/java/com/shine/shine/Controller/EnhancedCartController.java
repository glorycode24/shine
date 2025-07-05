package com.shine.shine.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shine.shine.Service.EnhancedCartService;
import com.shine.shine.dto.AddToCartRequestDto;
import com.shine.shine.dto.CartItemResponseDto;
import com.shine.shine.dto.UpdateQuantityRequestDto;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class EnhancedCartController {

    private final EnhancedCartService enhancedCartService;

    public EnhancedCartController(EnhancedCartService enhancedCartService) {
        this.enhancedCartService = enhancedCartService;
    }

    /**
     * Add item to cart
     */
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequestDto request) {
        try {
            CartItemResponseDto cartItem = enhancedCartService.addToCart(request);
            return new ResponseEntity<>(cartItem, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>("Operation failed: " + e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get current user's cart
     */
    @GetMapping("/items")
    public ResponseEntity<List<CartItemResponseDto>> getCartItems() {
        try {
            List<CartItemResponseDto> cartItems = enhancedCartService.getCurrentUserCart();
            return new ResponseEntity<>(cartItems, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update cart item quantity
     */
    @PutMapping("/update-quantity")
    public ResponseEntity<?> updateQuantity(@RequestBody UpdateQuantityRequestDto request) {
        try {
            CartItemResponseDto cartItem = enhancedCartService.updateQuantity(request);
            if (cartItem == null) {
                return new ResponseEntity<>("Item removed from cart", HttpStatus.OK);
            }
            return new ResponseEntity<>(cartItem, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>("Operation failed: " + e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Remove item from cart
     */
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Integer cartItemId) {
        try {
            boolean removed = enhancedCartService.removeFromCart(cartItemId);
            if (removed) {
                return new ResponseEntity<>("Item removed from cart", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Clear entire cart
     */
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart() {
        try {
            boolean cleared = enhancedCartService.clearCart();
            if (cleared) {
                return new ResponseEntity<>("Cart cleared successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No cart to clear", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get cart summary
     */
    @GetMapping("/summary")
    public ResponseEntity<EnhancedCartService.CartSummary> getCartSummary() {
        try {
            EnhancedCartService.CartSummary summary = enhancedCartService.getCartSummary();
            return new ResponseEntity<>(summary, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Check stock availability for a product variation
     */
    @GetMapping("/stock/{variationId}")
    public ResponseEntity<?> checkStock(@PathVariable Integer variationId) {
        try {
            // This would need to be implemented in the service or use the existing StockService
            // For now, returning a placeholder response
            return new ResponseEntity<>("Stock check endpoint - implement as needed", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 