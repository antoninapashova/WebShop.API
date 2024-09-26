package com.example.webshopapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CartItemDto {
    private String name;
    private int quantity;
    private double price;
}
