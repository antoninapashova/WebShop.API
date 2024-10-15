package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.OrderDto;
import com.example.webshopapi.dto.requestObjects.CreateOrderDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    ExecutionResult createOrder(CreateOrderDto orderDto);
    List<OrderDto> retrieveAllOrders();
    void setOrderStatus(UUID orderId, boolean isApproved);
}
