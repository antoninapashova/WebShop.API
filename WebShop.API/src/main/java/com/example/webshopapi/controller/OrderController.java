package com.example.webshopapi.controller;

import com.example.webshopapi.entity.UserPrinciple;
import com.example.webshopapi.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@AuthenticationPrincipal UserPrinciple principle)
    {
        orderService.createOrder(principle.getUserId());
        return ResponseEntity.ok().build();
    }
}
