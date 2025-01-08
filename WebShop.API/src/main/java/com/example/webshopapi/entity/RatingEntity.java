package com.example.webshopapi.entity;

import jakarta.persistence.*;
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

    private int score;

    @ManyToOne
    private ProductEntity product;

    public RatingEntity(int score, ProductEntity product) {
        this.score = score;
        this.product = product;
    }
}
