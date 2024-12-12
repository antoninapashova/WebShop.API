package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.PromotionDto;
import com.example.webshopapi.entity.ProductEntity;
import com.example.webshopapi.entity.PromotionEntity;
import com.example.webshopapi.entity.PromotionProduct;
import com.example.webshopapi.repository.ProductRepository;
import com.example.webshopapi.repository.PromotionProductRepository;
import com.example.webshopapi.repository.PromotionRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

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
    PromotionProductRepository promotionProductRepository;

    @Transactional
    @Override
    public ExecutionResult createPromotion(PromotionDto promotionDto) {
        PromotionEntity promotion = new PromotionEntity();
        promotion.setName(promotionDto.getName());
        promotion.setDiscount(promotionDto.getDiscount());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)", Locale.ENGLISH);

        ZonedDateTime startDateZoneTime = ZonedDateTime.parse(promotionDto.getStartDate(), formatter);
        LocalDateTime startDate = startDateZoneTime.toLocalDateTime();

        ZonedDateTime endDatezonedDateTime = ZonedDateTime.parse(promotionDto.getEndDate(), formatter);
        LocalDateTime endDate = endDatezonedDateTime.toLocalDateTime();

        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion.setActive(!startDate.isAfter(LocalDateTime.now()));

        promotionDto.getProductsInPromotion().forEach(e -> {
            PromotionProduct productPromotion = new PromotionProduct();

            ProductEntity productEntity = productRepository.findById(UUID.fromString(e))
                    .orElseThrow(() -> new EntityExistsException("Product not found!"));

            double newPrice = productEntity.getPrice() - productEntity.getPrice() * promotionDto.getDiscount() / 100;

            productPromotion.setProduct(productEntity);
            productPromotion.setPromotion(promotion);
            productPromotion.setPriceInPromotion(newPrice);

            promotionRepository.save(promotion);
            promotionProductRepository.save(productPromotion);
        });

        return new ExecutionResult("Promotion is set successfully!");
    }
}
