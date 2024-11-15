package com.example.webshopapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@Table(name = "cart_items")
@RequiredArgsConstructor
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int quantity;

    @ManyToOne
    private ProductEntity product;

    public CartItemEntity(int quantity, ProductEntity product) {
        this.quantity = quantity;
        this.product = product;
    }
}
