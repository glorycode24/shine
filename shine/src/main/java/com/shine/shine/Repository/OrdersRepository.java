package com.shine.shine.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shine.shine.Entity.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByUser_UserId(Integer userId);
    List<Orders> findByOrderStatus(String orderStatus);
 
}