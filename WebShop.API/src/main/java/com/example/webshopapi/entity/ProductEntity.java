package com.example.webshopapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity extends BaseEntity {
    private String name;
    private int quantity;
    private double price;
    private boolean isDeleted;
}