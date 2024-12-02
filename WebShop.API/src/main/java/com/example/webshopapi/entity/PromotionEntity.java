package com.example.webshopapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "promotions")
@RequiredArgsConstructor
public class PromotionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false, name = "discount %")
    private double discount;

    @OneToMany
    @JoinColumn(name = "promotion_id")
    private List<ProductEntity> productsInPromotion;
}
