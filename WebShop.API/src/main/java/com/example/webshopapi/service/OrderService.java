package com.example.webshopapi.service;

import com.example.webshopapi.dto.OrderDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    void createOrder(UUID userId);
    List<OrderDto> retrieveAllOrders();
    void setOrderStatus(UUID orderId, boolean isApproved);
}
