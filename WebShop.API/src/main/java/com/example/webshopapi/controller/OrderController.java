package com.example.webshopapi.controller;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.*;
import com.example.webshopapi.dto.requestObjects.SetOrderStatusRequest;
import com.example.webshopapi.entity.UserPrinciple;
import com.example.webshopapi.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<CreateOrderResponse> createOrder(@AuthenticationPrincipal UserPrinciple principle, @ModelAttribute CreateOrderDto order) {
        order.setUserId(principle.getUserId());
        CreateOrderResponse result = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/all-orders")
    public ResponseEntity<List<OrderDto>> retrieveAllOrders() {
        List<OrderDto> orderDtoList = orderService.retrieveAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(orderDtoList);
    }

    @PutMapping("/set-order-approved")
    public ResponseEntity<ExecutionResult> setOrderApproved(@RequestBody SetOrderStatusRequest request) {
        ExecutionResult result = orderService.setOrderStatus(UUID.fromString(request.orderId), request.isApproved);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/set-order-status/{orderId}/{status}")
    public ResponseEntity<ExecutionResult> changeOrderStatus(@PathVariable String orderId, @PathVariable String status) {
        ExecutionResult result = orderService.changeOrderStatus(UUID.fromString(orderId), status);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/get-order-items/{orderId}")
    public ResponseEntity<List<OrderItemDto>> getOrderItems(@PathVariable String orderId) {
        List<OrderItemDto> result = orderService.getOrderItems(UUID.fromString(orderId));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/get-order/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable String orderId){
        OrderDto result = orderService.getOrderById(UUID.fromString(orderId));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/user/orders")
    public ResponseEntity<List<UserOrderDto>> getUserOrders(@AuthenticationPrincipal UserPrinciple user){
        List<UserOrderDto> orders = orderService.getUserOrders(user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @GetMapping("/order/analytics")
    public ResponseEntity<AnalyticsResponse> getAnalytics(){
        return ResponseEntity.ok(orderService.calculateAnalytics());
    }
}
