package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.*;
import com.example.webshopapi.dto.requestObjects.CreateOrderRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    CreateOrderResponse createOrder(CreateOrderRequest orderDto);
    List<OrderDto> retrieveAllOrders();
    ExecutionResult setOrderStatus(UUID orderId, boolean isApproved);
    ExecutionResult changeOrderStatus(UUID orderId, String status);
    List<OrderItemDto> getOrderItems(UUID orderId);
    OrderDto getOrderById(UUID orderId);
    List<UserOrderDto> getUserOrders(UUID userId);
    AnalyticsResponse calculateAnalytics();
}
