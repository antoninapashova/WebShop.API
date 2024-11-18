package com.example.webshopapi.controller;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.service.SubscriberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SubscriberController {
    private final SubscriberService subscriberService;

    @PostMapping("/subscribe/{email}")
    public ResponseEntity<?> subscribe(@PathVariable String email) {
        ExecutionResult result = subscriberService.subscribe(email);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
