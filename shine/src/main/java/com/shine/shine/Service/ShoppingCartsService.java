package com.shine.shine.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shine.shine.Entity.ShoppingCarts;
import com.shine.shine.Entity.Users;
import com.shine.shine.Repository.ShoppingCartsRepository;
import com.shine.shine.Repository.UsersRepository;

@Service
public class ShoppingCartsService {

    private final ShoppingCartsRepository shoppingCartRepository;
    private final UsersRepository userRepository;

    public ShoppingCartsService(ShoppingCartsRepository shoppingCartRepository, UsersRepository userRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
    }

    public List<ShoppingCarts> getAllShoppingCarts() {
        return shoppingCartRepository.findAll();
    }

    public Optional<ShoppingCarts> getShoppingCartById(Integer id) {
        return shoppingCartRepository.findById(id);
    }

    @Transactional
    public ShoppingCarts createShoppingCart(ShoppingCarts shoppingCart) {
        Users user = userRepository.findById(shoppingCart.getUser().getUserId())
                                   .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + shoppingCart.getUser().getUserId()));
        shoppingCart.setUser(user);

        if (shoppingCartRepository.findByUser_UserId(user.getUserId()).isPresent()) {
            throw new IllegalArgumentException("User with ID " + user.getUserId() + " already has a shopping cart.");
        }

        if (shoppingCart.getCreationDate() == null) {
            shoppingCart.setCreationDate(LocalDateTime.now());
        }

        return shoppingCartRepository.save(shoppingCart);
    }

    @Transactional
    public ShoppingCarts updateShoppingCart(Integer id, ShoppingCarts shoppingCartDetails) {
        Optional<ShoppingCarts> optionalShoppingCart = shoppingCartRepository.findById(id);
        if (optionalShoppingCart.isPresent()) {
            ShoppingCarts existingCart = optionalShoppingCart.get();

            if (shoppingCartDetails.getUser() != null && shoppingCartDetails.getUser().getUserId() != null) {
                if (!existingCart.getUser().getUserId().equals(shoppingCartDetails.getUser().getUserId())) {
                    Users newUser = userRepository.findById(shoppingCartDetails.getUser().getUserId())
                                                  .orElseThrow(() -> new IllegalArgumentException("New user not found for ID: " + shoppingCartDetails.getUser().getUserId()));
                    if (shoppingCartRepository.findByUser_UserId(newUser.getUserId()).isPresent()) {
                        throw new IllegalArgumentException("New user with ID " + newUser.getUserId() + " already has a shopping cart.");
                    }
                    existingCart.setUser(newUser);
                }
            }

            return shoppingCartRepository.save(existingCart);
        } else {
            return null; 
        }
    }

    @Transactional
    public boolean deleteShoppingCart(Integer id) {
        if (shoppingCartRepository.existsById(id)) {
            shoppingCartRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<ShoppingCarts> getShoppingCartByUserId(Integer userId) {
        return shoppingCartRepository.findByUser_UserId(userId);
    }
}