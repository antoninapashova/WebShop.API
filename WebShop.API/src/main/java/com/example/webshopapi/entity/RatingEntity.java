package com.example.webshopapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Table(name = "ratings")
@RequiredArgsConstructor
public class RatingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private int score;

    @ManyToOne
    private ProductEntity product;

    public RatingEntity(int score, ProductEntity product) {
        this.score = score;
        this.product = product;
    }
}
