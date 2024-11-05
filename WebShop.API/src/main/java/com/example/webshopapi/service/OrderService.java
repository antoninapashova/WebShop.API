package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.config.result.TypedResult;
import com.example.webshopapi.dto.OrderDto;
import com.example.webshopapi.dto.OrderItemDto;
import com.example.webshopapi.dto.UserOrderDto;
import com.example.webshopapi.dto.requestObjects.CreateOrderDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    ExecutionResult createOrder(CreateOrderDto orderDto);
    List<OrderDto> retrieveAllOrders();
    ExecutionResult setOrderStatus(UUID orderId, boolean isApproved);
    ExecutionResult changeOrderStatus(UUID orderId, String status);
    TypedResult<List<OrderItemDto>> getOrderItems(UUID orderId);
    TypedResult<OrderDto> getOrderById(UUID orderId);
    List<UserOrderDto> getUserOrders(UUID userId);
}
