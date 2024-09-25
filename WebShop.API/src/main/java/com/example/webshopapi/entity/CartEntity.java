package com.example.webshopapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "carts")
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private UUID userID;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItemEntity> items;
}
