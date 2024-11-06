package com.example.webshopapi.repository;

import com.example.webshopapi.entity.OrderEntity;
import com.example.webshopapi.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> findAllByUserId(UUID userId);
    List<OrderEntity> findByOrderDateBetweenAndOrderStatus(LocalDateTime startOfMonth, LocalDateTime endOfMonth, OrderStatus status);
    Long countByOrderStatus(OrderStatus status);
}
