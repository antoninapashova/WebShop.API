package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.PromotionDto;
import com.example.webshopapi.entity.ProductEntity;
import com.example.webshopapi.entity.PromotionEntity;
import com.example.webshopapi.repository.ProductRepository;
import com.example.webshopapi.repository.PromotionRepository;
import jakarta.persistence.EntityExistsException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PromotionServiceImpl implements PromotionService {
    PromotionRepository promotionRepository;
    ProductRepository productRepository;

    @Override
    public ExecutionResult createPromotion(PromotionDto promotionDto) throws ParseException {
        PromotionEntity promotion = new PromotionEntity();
        promotion.setDiscount(promotionDto.getDiscount());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)", Locale.ENGLISH);

        ZonedDateTime startDateZoneTime = ZonedDateTime.parse(promotionDto.getStartDate(), formatter);
        LocalDateTime startDate = startDateZoneTime.toLocalDateTime();

        ZonedDateTime endDatezonedDateTime = ZonedDateTime.parse(promotionDto.getEndDate(), formatter);
        LocalDateTime endDate = endDatezonedDateTime.toLocalDateTime();

        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);

        List<ProductEntity> products = new ArrayList<>();

        promotionDto.getProductsInPromotion().forEach(e -> {
            ProductEntity productEntity = productRepository.findById(UUID.fromString(e))
                    .orElseThrow(() -> new EntityExistsException("You already have subscribed to our newsletter!"));

            double newPrice = productEntity.getPrice() - productEntity.getPrice() * promotionDto.getDiscount() / 100;
            productEntity.setPromotionPrice(newPrice);
            products.add(productEntity);
        });

        promotion.setProductsInPromotion(products);

        promotionRepository.save(promotion);
        return new ExecutionResult("Promotion is set successfully!");
    }
}
