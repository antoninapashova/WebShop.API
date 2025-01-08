package com.example.webshopapi.controller;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.requestObjects.CreateRatingRequest;
import com.example.webshopapi.service.RatingService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingController {
    RatingService ratingService;

    @PostMapping("/add-rating")
    public ResponseEntity<?> addRating(@RequestBody CreateRatingRequest createRatingRequest) throws Exception {
        ExecutionResult result = ratingService.addRating(createRatingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
