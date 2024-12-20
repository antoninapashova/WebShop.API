package com.example.webshopapi.controller;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.service.SubscriberService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriberController {
    SubscriberService subscriberService;

    @PostMapping("/subscribe/{email}")
    public ResponseEntity<?> subscribe(@PathVariable String email) {
        ExecutionResult result = subscriberService.subscribe(email);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
