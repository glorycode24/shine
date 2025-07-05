package com.shine.shine.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shine.shine.Entity.CartItems;
import com.shine.shine.Entity.ProductVariations;
import com.shine.shine.Entity.ShoppingCarts;
import com.shine.shine.Entity.Users;
import com.shine.shine.Repository.CartItemsRepository;
import com.shine.shine.Repository.ShoppingCartsRepository;
import com.shine.shine.Repository.UsersRepository;
import com.shine.shine.dto.AddToCartRequestDto;
import com.shine.shine.dto.CartItemResponseDto;
import com.shine.shine.dto.UpdateQuantityRequestDto;

@Service
public class EnhancedCartService {

    private final CartItemsRepository cartItemsRepository;
    private final ShoppingCartsRepository shoppingCartsRepository;
    private final UsersRepository usersRepository;
    private final StockService stockService;

    public EnhancedCartService(CartItemsRepository cartItemsRepository,
                             ShoppingCartsRepository shoppingCartsRepository,
                             UsersRepository usersRepository,
                             StockService stockService) {
        this.cartItemsRepository = cartItemsRepository;
        this.shoppingCartsRepository = shoppingCartsRepository;
        this.usersRepository = usersRepository;
        this.stockService = stockService;
    }

    /**
     * Get current authenticated user
     */
    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalStateException("User not found: " + userEmail));
    }

    /**
     * Get or create shopping cart for current user
     */
    private ShoppingCarts getOrCreateShoppingCart(Users user) {
        Optional<ShoppingCarts> existingCart = shoppingCartsRepository.findByUser_UserId(user.getUserId());
        if (existingCart.isPresent()) {
            return existingCart.get();
        }

        ShoppingCarts newCart = new ShoppingCarts(user, LocalDateTime.now());
        return shoppingCartsRepository.save(newCart);
    }

    /**
     * Add item to cart with stock validation
     */
    @Transactional
    public CartItemResponseDto addToCart(AddToCartRequestDto request) {
        // Validate request
        if (request.getVariationId() == null || request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Invalid request: variationId and quantity must be positive");
        }

        // Get current user
        Users currentUser = getCurrentUser();

        // Check stock availability
        if (!stockService.hasSufficientStock(request.getVariationId(), request.getQuantity())) {
            throw new IllegalStateException("Insufficient stock for the requested quantity");
        }

        // Get or create shopping cart
        ShoppingCarts shoppingCart = getOrCreateShoppingCart(currentUser);

        // Get product variation
        ProductVariations productVariation = stockService.getProductVariation(request.getVariationId())
                .orElseThrow(() -> new IllegalArgumentException("Product variation not found"));

        // Check if item already exists in cart
        Optional<CartItems> existingCartItem = cartItemsRepository
                .findByShoppingCart_CartIdAndProductVariation_VariationId(
                        shoppingCart.getCartId(), request.getVariationId());

        CartItems cartItem;
        if (existingCartItem.isPresent()) {
            // Update existing cart item
            cartItem = existingCartItem.get();
            int newQuantity = cartItem.getQuantity() + request.getQuantity();
            
            // Check if new total quantity exceeds stock
            if (!stockService.hasSufficientStock(request.getVariationId(), newQuantity)) {
                throw new IllegalStateException("Insufficient stock for the total quantity");
            }
            
            cartItem.setQuantity(newQuantity);
        } else {
            // Create new cart item
            cartItem = new CartItems(shoppingCart, productVariation, request.getQuantity());
            cartItem.setProduct(productVariation.getProduct());
        }

        // Save cart item
        cartItem = cartItemsRepository.save(cartItem);

        // Convert to response DTO
        return convertToResponseDto(cartItem);
    }

    /**
     * Update cart item quantity
     */
    @Transactional
    public CartItemResponseDto updateQuantity(UpdateQuantityRequestDto request) {
        // Validate request
        if (request.getCartItemId() == null || request.getQuantity() == null) {
            throw new IllegalArgumentException("Invalid request: cartItemId and quantity are required");
        }

        // Get current user
        Users currentUser = getCurrentUser();

        // Get cart item
        CartItems cartItem = cartItemsRepository.findById(request.getCartItemId())
                .orElseGet(() -> null);

        if (cartItem == null) {
            throw new IllegalArgumentException("Cart item not found");
        }

        // Verify ownership
        if (!cartItem.getShoppingCart().getUser().getUserId().equals(currentUser.getUserId())) {
            throw new IllegalStateException("Unauthorized access to cart item");
        }

        // Handle quantity update
        if (request.getQuantity() <= 0) {
            // Remove item if quantity is 0 or negative
            cartItemsRepository.delete(cartItem);
            return null;
        }

        // Check stock availability for new quantity
        if (!stockService.hasSufficientStock(cartItem.getProductVariation().getVariationId(), request.getQuantity())) {
            throw new IllegalStateException("Insufficient stock for the requested quantity");
        }

        // Update quantity
        cartItem.setQuantity(request.getQuantity());
        cartItem = cartItemsRepository.save(cartItem);

        return convertToResponseDto(cartItem);
    }

    /**
     * Remove item from cart
     */
    @Transactional
    public boolean removeFromCart(Integer cartItemId) {
        // Get current user
        Users currentUser = getCurrentUser();

        // Get cart item
        CartItems cartItem = cartItemsRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        // Verify ownership
        if (!cartItem.getShoppingCart().getUser().getUserId().equals(currentUser.getUserId())) {
            throw new IllegalStateException("Unauthorized access to cart item");
        }

        cartItemsRepository.delete(cartItem);
        return true;
    }

    /**
     * Get current user's cart items
     */
    public List<CartItemResponseDto> getCurrentUserCart() {
        Users currentUser = getCurrentUser();
        
        Optional<ShoppingCarts> shoppingCart = shoppingCartsRepository.findByUser_UserId(currentUser.getUserId());
        if (shoppingCart.isEmpty()) {
            return List.of(); // Return empty list if no cart exists
        }

        List<CartItems> cartItems = cartItemsRepository.findByShoppingCart_CartId(shoppingCart.get().getCartId());
        
        return cartItems.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Clear current user's cart
     */
    @Transactional
    public boolean clearCart() {
        Users currentUser = getCurrentUser();
        
        Optional<ShoppingCarts> shoppingCart = shoppingCartsRepository.findByUser_UserId(currentUser.getUserId());
        if (shoppingCart.isEmpty()) {
            return true; // No cart to clear
        }

        List<CartItems> cartItems = cartItemsRepository.findByShoppingCart_CartId(shoppingCart.get().getCartId());
        cartItemsRepository.deleteAll(cartItems);
        
        return true;
    }

    /**
     * Get cart summary (total items, total price)
     */
    public CartSummary getCartSummary() {
        List<CartItemResponseDto> cartItems = getCurrentUserCart();
        
        int totalItems = cartItems.stream()
                .mapToInt(CartItemResponseDto::getQuantity)
                .sum();
        
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItemResponseDto::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return new CartSummary(totalItems, totalPrice);
    }

    /**
     * Convert CartItems entity to CartItemResponseDto
     */
    private CartItemResponseDto convertToResponseDto(CartItems cartItem) {
        ProductVariations variation = cartItem.getProductVariation();
        Integer availableStock = stockService.getAvailableStock(variation.getVariationId());
        
        BigDecimal productPrice = variation.getPrice();
BigDecimal totalPrice = productPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        
        return new CartItemResponseDto(
                cartItem.getCartItemId(),
                cartItem.getProduct().getProductId(),
                cartItem.getProduct().getProductName(),
                cartItem.getProduct().getImageUrl(),
                productPrice,
                variation.getVariationId(),
                variation.getSize().getSizeName(),
                variation.getColor().getColorName(),
                cartItem.getQuantity(),
                availableStock,
                totalPrice
        );
    }

    /**
     * Inner class for cart summary
     */
    public static class CartSummary {
        private final int totalItems;
        private final BigDecimal totalPrice;

        public CartSummary(int totalItems, BigDecimal totalPrice) {
            this.totalItems = totalItems;
            this.totalPrice = totalPrice;
        }

        public int getTotalItems() {
            return totalItems;
        }

        public BigDecimal getTotalPrice() {
            return totalPrice;
        }
    }
} 