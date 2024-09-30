package com.example.webshopapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "carts")
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity extends BaseEntity {

    @OneToOne
    @JoinColumn(name="user_id")
    private UserEntity user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cart", cascade = CascadeType.REMOVE)
    private List<CartItemEntity> items;
}
