package com.example.webshopapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CartItemDto {
    private String id;
    private String name;
    private int quantity;
    private double price;
    public byte[] img;
}
