package com.example.webshopapi.repository;

import com.example.webshopapi.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    @Query(value = "SELECT * FROM order_items WHERE order_id = :orderId", nativeQuery = true)
    List<OrderItem> findAllByOrderId(@Param("orderId") UUID orderId);
}
