package com.example.webshopapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "promotion_product")
@Data
@RequiredArgsConstructor
public class PromotionProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "promotion_id", nullable = false)
    private PromotionEntity promotion;

    private boolean isActive;
    private boolean priceInPromotion;
}
