package com.example.webshopapi.controller;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.config.result.FailureType;
import com.example.webshopapi.dto.OrderDto;
import com.example.webshopapi.dto.requestObjects.CreateOrderDto;
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
    public ResponseEntity<?> createOrder(@AuthenticationPrincipal UserPrinciple principle, @RequestBody CreateOrderDto order) {
        order.setUserId(principle.getUserId());
        ExecutionResult result = orderService.createOrder(order);

        if (result.getFailureType() == FailureType.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/all-orders")
    public ResponseEntity<List<OrderDto>> retrieveAllOrders() {
        List<OrderDto> orderDtoList = orderService.retrieveAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(orderDtoList);
    }

    @PutMapping("/set-order-approved")
    public ResponseEntity<?> setOrderApproved(@RequestBody SetOrderStatusRequest request) {
        if (request == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        try {
            orderService.setOrderStatus(UUID.fromString(request.orderId), request.isApproved);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
