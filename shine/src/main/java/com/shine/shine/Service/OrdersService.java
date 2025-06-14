package com.shine.shine.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shine.shine.Entity.Addresses;
import com.shine.shine.Entity.Orders;
import com.shine.shine.Entity.Users;
import com.shine.shine.Repository.AddressesRepository;
import com.shine.shine.Repository.OrdersRepository;
import com.shine.shine.Repository.UsersRepository;

@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final UsersRepository usersRepository;
    private final AddressesRepository addressesRepository;

    public OrdersService(OrdersRepository orderRepository, UsersRepository userRepository, AddressesRepository addressRepository) {
        this.ordersRepository = orderRepository;
        this.usersRepository = userRepository;
        this.addressesRepository = addressRepository;
    }

    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    public Optional<Orders> getOrderById(Integer id) {
        return ordersRepository.findById(id);
    }

    @Transactional
    public Orders createOrder(Orders order) {
        Users user = usersRepository.findById(order.getUser().getUserId())
                                 .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + order.getUser().getUserId()));
        order.setUser(user);

        Addresses shippingAddress = addressesRepository.findById(order.getShippingAddress().getAddressId())
                                                  .orElseThrow(() -> new IllegalArgumentException("Shipping address not found for ID: " + order.getShippingAddress().getAddressId()));
        order.setShippingAddress(shippingAddress);

        Addresses billingAddress = addressesRepository.findById(order.getBillingAddress().getAddressId())
                                                 .orElseThrow(() -> new IllegalArgumentException("Billing address not found for ID: " + order.getBillingAddress().getAddressId()));
        order.setBillingAddress(billingAddress);

        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }
        if (order.getOrderStatus() == null || order.getOrderStatus().isEmpty()) {
            order.setOrderStatus("PENDING");
        }

        return ordersRepository.save(order);
    }

    @Transactional
    public Orders updateOrder(Integer id, Orders orderDetails) {
        Optional<Orders> optionalOrder = ordersRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Orders existingOrder = optionalOrder.get();

            if (orderDetails.getUser() != null && orderDetails.getUser().getUserId() != null) {
                Users user = usersRepository.findById(orderDetails.getUser().getUserId())
                                         .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + orderDetails.getUser().getUserId()));
                existingOrder.setUser(user);
            }

            if (orderDetails.getShippingAddress() != null && orderDetails.getShippingAddress().getAddressId() != null) {
                Addresses shippingAddress = addressesRepository.findById(orderDetails.getShippingAddress().getAddressId())
                                                          .orElseThrow(() -> new IllegalArgumentException("Shipping address not found for ID: " + orderDetails.getShippingAddress().getAddressId()));
                existingOrder.setShippingAddress(shippingAddress);
            }
            if (orderDetails.getBillingAddress() != null && orderDetails.getBillingAddress().getAddressId() != null) {
                Addresses billingAddress = addressesRepository.findById(orderDetails.getBillingAddress().getAddressId())
                                                         .orElseThrow(() -> new IllegalArgumentException("Billing address not found for ID: " + orderDetails.getBillingAddress().getAddressId()));
                existingOrder.setBillingAddress(billingAddress);
            }

            existingOrder.setOrderDate(orderDetails.getOrderDate() != null ? orderDetails.getOrderDate() : existingOrder.getOrderDate());
            existingOrder.setOrderStatus(orderDetails.getOrderStatus() != null ? orderDetails.getOrderStatus() : existingOrder.getOrderStatus());
            existingOrder.setTotalAmount(orderDetails.getTotalAmount() != null ? orderDetails.getTotalAmount() : existingOrder.getTotalAmount());

            return ordersRepository.save(existingOrder);
        } else {
            return null;
        }
    }

    @Transactional
    public boolean deleteOrder(Integer id) {
        if (ordersRepository.existsById(id)) {
        
            ordersRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Orders> getOrdersByUserId(Integer userId) {
        return ordersRepository.findByUser_UserId(userId);
    }

    public List<Orders> getOrdersByStatus(String status) {
        return ordersRepository.findByOrderStatus(status);
    }
}