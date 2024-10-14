package com.example.webshopapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "cart_items")
@NoArgsConstructor
@AllArgsConstructor
public class CartItemEntity extends BaseEntity {
    private String name;
    private int quantity;
    private double price;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    private CartEntity cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @OnDelete(action= OnDeleteAction.CASCADE)
    private ProductEntity product;
}
