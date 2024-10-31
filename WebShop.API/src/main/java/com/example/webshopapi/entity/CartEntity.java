package com.example.webshopapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Table(name = "carts")
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    private UserEntity user;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private List<CartItemEntity> items;

    public CartEntity(UserEntity user, List<CartItemEntity> items) {
        this.user = user;
        this.items = items;
    }
}
