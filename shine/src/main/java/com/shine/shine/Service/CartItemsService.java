package com.shine.shine.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shine.shine.Entity.CartItems;
import com.shine.shine.Entity.ProductVariations;
import com.shine.shine.Entity.ShoppingCarts;
import com.shine.shine.Repository.CartItemsRepository;
import com.shine.shine.Repository.ProductVariationsRepository;
import com.shine.shine.Repository.ShoppingCartsRepository;

@Service
public class CartItemsService {

    private final CartItemsRepository cartItemsRepository;
    private final ShoppingCartsRepository shoppingCartsRepository;
    private final ProductVariationsRepository productVariationsRepository;

    public CartItemsService(CartItemsRepository cartItemRepository,
                           ShoppingCartsRepository shoppingCartRepository,
                           ProductVariationsRepository productVariationRepository) {
        this.cartItemsRepository = cartItemRepository;
        this.shoppingCartsRepository = shoppingCartRepository;
        this.productVariationsRepository = productVariationRepository;
    }

    public List<CartItems> getAllCartItems() {
        return cartItemsRepository.findAll();
    }

    public Optional<CartItems> getCartItemById(Integer id) {
        return cartItemsRepository.findById(id);
    }

    @Transactional
    public CartItems createCartItem(CartItems cartItem) {
      
        ShoppingCarts sCart = shoppingCartsRepository.findById(cartItem.getShoppingCart().getCartId())
                                                  .orElseThrow(() -> new IllegalArgumentException("Shopping Cart not found for ID: " + cartItem.getShoppingCart().getCartId()));
        cartItem.setShoppingCart(sCart);

      
        ProductVariations productVariation = productVariationsRepository.findById(cartItem.getProductVariation().getVariationId())
                                                                     .orElseThrow(() -> new IllegalArgumentException("Product Variation not found for ID: " + cartItem.getProductVariation().getVariationId()));
        cartItem.setProductVariation(productVariation);

     
        Optional<CartItems> existingCartItem = cartItemsRepository.findByShoppingCart_CartIdAndProductVariation_VariationId(
            sCart.getCartId(), productVariation.getVariationId());

        if (existingCartItem.isPresent()) {
           
            CartItems itemToUpdate = existingCartItem.get();
            itemToUpdate.setQuantity(itemToUpdate.getQuantity() + cartItem.getQuantity());
            return cartItemsRepository.save(itemToUpdate);
        } else {
            return cartItemsRepository.save(cartItem);
        }
    }

    @Transactional
    public CartItems updateCartItem(Integer id, CartItems cartItemDetails) {
        Optional<CartItems> optionalCartItem = cartItemsRepository.findById(id);
        if (optionalCartItem.isPresent()) {
            CartItems existingCartItem = optionalCartItem.get();

           
            if (cartItemDetails.getShoppingCart() != null && cartItemDetails.getShoppingCart().getCartId() != null) {
                ShoppingCarts sCart = shoppingCartsRepository.findById(cartItemDetails.getShoppingCart().getCartId())
                                                          .orElseThrow(() -> new IllegalArgumentException("Shopping Cart not found for ID: " + cartItemDetails.getShoppingCart().getCartId()));
                existingCartItem.setShoppingCart(sCart);
            }

        
            if (cartItemDetails.getProductVariation() != null && cartItemDetails.getProductVariation().getVariationId() != null) {
                ProductVariations productVariation = productVariationsRepository.findById(cartItemDetails.getProductVariation().getVariationId())
                                                                             .orElseThrow(() -> new IllegalArgumentException("Product Variation not found for ID: " + cartItemDetails.getProductVariation().getVariationId()));
                existingCartItem.setProductVariation(productVariation);
            }

            existingCartItem.setQuantity(cartItemDetails.getQuantity() != null ? cartItemDetails.getQuantity() : existingCartItem.getQuantity());
            if (existingCartItem.getQuantity() <= 0) {
                cartItemsRepository.delete(existingCartItem); // Remove item if quantity becomes 0 or less
                return null;
            }
            return cartItemsRepository.save(existingCartItem);
        } else {
            return null; 
        }
    }

    @Transactional
    public boolean deleteCartItem(Integer id) {
        if (cartItemsRepository.existsById(id)) {
            cartItemsRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<CartItems> getCartItemsByShoppingCartId(Integer cartId) {
        return cartItemsRepository.findByShoppingCart_CartId(cartId);
    }
}