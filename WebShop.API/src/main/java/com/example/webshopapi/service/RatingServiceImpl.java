package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.requestObjects.CreateRatingRequest;
import com.example.webshopapi.entity.ProductEntity;
import com.example.webshopapi.entity.RatingEntity;
import com.example.webshopapi.repository.ProductRepository;
import com.example.webshopapi.repository.RatingRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingServiceImpl implements RatingService {
    private ProductRepository productRepository;
    private RatingRepository ratingRepository;

    public ExecutionResult addRating(CreateRatingRequest request) {
        Optional<ProductEntity> product = productRepository.findById(UUID.fromString(request.getProductId()));

        if (product.isPresent() && request.getScore() >= 1 && request.getScore() <= 5) {
            RatingEntity rating = new RatingEntity(request.getScore(), product.get());
            ratingRepository.save(rating);
            return new ExecutionResult("Rating send successfully!");
        }

        return null;
    }
}
