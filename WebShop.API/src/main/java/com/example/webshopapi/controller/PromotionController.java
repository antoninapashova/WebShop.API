package com.example.webshopapi.controller;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.PromotionDto;
import com.example.webshopapi.service.PromotionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@AllArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @PostMapping("/create-promotion")
    public ResponseEntity<ExecutionResult> addProduct(@ModelAttribute PromotionDto promotionDto) throws ParseException {
        ExecutionResult result = promotionService.createPromotion(promotionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
