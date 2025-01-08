package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.requestObjects.CreateRatingRequest;

public interface RatingService {
    ExecutionResult addRating(CreateRatingRequest createRatingRequest);
}
