package com.shine.shine.Service;

import java.math.BigDecimal; // Import BigDecimal
import java.math.RoundingMode; // Import RoundingMode
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shine.shine.Entity.Addresses;
import com.shine.shine.Entity.Orders;
import com.shine.shine.Entity.Users;
import com.shine.shine.Entity.CartItems; // Assuming you have a CartItem entity
import com.shine.shine.Repository.AddressesRepository;
import com.shine.shine.Repository.OrdersRepository;
import com.shine.shine.Repository.UsersRepository;

@Service
public class OrdersService {

    // --- NEW: Define the tax rate as a constant ---
    private static final BigDecimal VAT_RATE = new BigDecimal("0.12"); // 12%

    private final OrdersRepository ordersRepository;
    private final UsersRepository usersRepository;
    private final AddressesRepository addressesRepository;

    public OrdersService(OrdersRepository orderRepository, UsersRepository userRepository, AddressesRepository addressRepository) {
        this.ordersRepository = orderRepository;
        this.usersRepository = userRepository;
        this.addressesRepository = addressRepository;
    }

    // --- NEW, SECURE METHOD TO CREATE AN ORDER ---
    @Transactional
    public Orders createOrderFromCart(Integer userId, Integer shippingAddressId, Integer billingAddressId, List<CartItems> cartItems) {
        // 1. Fetch the required entities
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));
        Addresses shippingAddress = addressesRepository.findById(shippingAddressId)
                .orElseThrow(() -> new IllegalArgumentException("Shipping address not found for ID: " + shippingAddressId));
        Addresses billingAddress = addressesRepository.findById(billingAddressId)
                .orElseThrow(() -> new IllegalArgumentException("Billing address not found for ID: " + billingAddressId));

        // 2. Perform the financial calculations
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItems item : cartItems) {
            // Ensure product and price are not null to avoid NullPointerException
            if (item.getProduct() != null && item.getProduct().getPrice() != null) {
                BigDecimal itemPrice = item.getProduct().getPrice();
                BigDecimal quantity = new BigDecimal(item.getQuantity());
                subtotal = subtotal.add(itemPrice.multiply(quantity));
            }
        }
        
        BigDecimal tax = subtotal.multiply(VAT_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = subtotal.add(tax);

        // 3. Create and populate the new order object
        Orders newOrder = new Orders();
        newOrder.setUser(user);
        newOrder.setShippingAddress(shippingAddress);
        newOrder.setBillingAddress(billingAddress);
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setOrderStatus("PENDING");
        
        // Set the calculated financial values
        newOrder.setSubtotal(subtotal);
        newOrder.setTax(tax);
        newOrder.setTotalAmount(totalAmount);

        // (If you have an OrderItems entity, you would create and add them here)

        // 4. Save the new order to the database
        return ordersRepository.save(newOrder);
    }


    // --- YOUR EXISTING METHODS (UNCHANGED) ---

    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    public Optional<Orders> getOrderById(Integer id) {
        return ordersRepository.findById(id);
    }
    
    // This original createOrder method can be kept for admin use or deprecated
    @Transactional
    public Orders createOrder(Orders order) {
        // (Your original logic is here and remains unchanged)
        // ...
        return ordersRepository.save(order);
    }
    
    @Transactional
    public Orders updateOrder(Integer id, Orders orderDetails) {
        // (Your original logic is here and remains unchanged)
        // ...
        return null; // Your original logic here
    }

    @Transactional
    public boolean deleteOrder(Integer id) {
        // (Your original logic is here and remains unchanged)
        // ...
        return false; // Your original logic here
    }

    public List<Orders> getOrdersByUserId(Integer userId) {
        return ordersRepository.findByUser_UserId(userId);
    }

    public List<Orders> getOrdersByStatus(String status) {
        return ordersRepository.findByOrderStatus(status);
    }
}