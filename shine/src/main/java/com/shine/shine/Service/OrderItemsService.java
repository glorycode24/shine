package com.shine.shine.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shine.shine.Entity.OrderItems;
import com.shine.shine.Entity.Orders;
import com.shine.shine.Entity.ProductVariations;
import com.shine.shine.Repository.OrderItemsRepository;
import com.shine.shine.Repository.OrdersRepository;
import com.shine.shine.Repository.ProductVariationsRepository;

@Service
public class OrderItemsService {

    private final OrderItemsRepository orderItemsRepository;
    private final OrdersRepository ordersRepository;
    private final ProductVariationsRepository productVariationsRepository;

    public OrderItemsService(OrderItemsRepository orderItemRepository,
                            OrdersRepository orderRepository,
                            ProductVariationsRepository productVariationRepository) {
        this.orderItemsRepository = orderItemRepository;
        this.ordersRepository = orderRepository;
        this.productVariationsRepository = productVariationRepository;
    }

    public List<OrderItems> getAllOrderItems() {
        return orderItemsRepository.findAll();
    }

    public Optional<OrderItems> getOrderItemById(Integer id) {
        return orderItemsRepository.findById(id);
    }

    @Transactional
    public OrderItems createOrderItem(OrderItems orderItem) {
   
        Orders order = ordersRepository.findById(orderItem.getOrder().getOrderId())
                                     .orElseThrow(() -> new IllegalArgumentException("Order not found for ID: " + orderItem.getOrder().getOrderId()));
        orderItem.setOrder(order);

       
        ProductVariations productVariation = productVariationsRepository.findById(orderItem.getProductVariation().getVariationId())
                                                                     .orElseThrow(() -> new IllegalArgumentException("Product Variation not found for ID: " + orderItem.getProductVariation().getVariationId()));
        orderItem.setProductVariation(productVariation);

        return orderItemsRepository.save(orderItem);
    }

    @Transactional
    public OrderItems updateOrderItem(Integer id, OrderItems orderItemDetails) {
        Optional<OrderItems> optionalOrderItem = orderItemsRepository.findById(id);
        if (optionalOrderItem.isPresent()) {
            OrderItems existingOrderItem = optionalOrderItem.get();

            if (orderItemDetails.getOrder() != null && orderItemDetails.getOrder().getOrderId() != null) {
                Orders order = ordersRepository.findById(orderItemDetails.getOrder().getOrderId())
                                             .orElseThrow(() -> new IllegalArgumentException("Order not found for ID: " + orderItemDetails.getOrder().getOrderId()));
                existingOrderItem.setOrder(order);
            }

            if (orderItemDetails.getProductVariation() != null && orderItemDetails.getProductVariation().getVariationId() != null) {
                ProductVariations productVariation = productVariationsRepository.findById(orderItemDetails.getProductVariation().getVariationId())
                                                                             .orElseThrow(() -> new IllegalArgumentException("Product Variation not found for ID: " + orderItemDetails.getProductVariation().getVariationId()));
                existingOrderItem.setProductVariation(productVariation);
            }

            existingOrderItem.setQuantity(orderItemDetails.getQuantity() != null ? orderItemDetails.getQuantity() : existingOrderItem.getQuantity());
            existingOrderItem.setPricePerUnit(orderItemDetails.getPricePerUnit() != null ? orderItemDetails.getPricePerUnit() : existingOrderItem.getPricePerUnit());

            return orderItemsRepository.save(existingOrderItem);
        } else {
            return null; 
        }
    }

    @Transactional
    public boolean deleteOrderItem(Integer id) {
        if (orderItemsRepository.existsById(id)) {
            orderItemsRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<OrderItems> getOrderItemsByOrderId(Integer orderId) {
        return orderItemsRepository.findByOrder_OrderId(orderId);
    }
}