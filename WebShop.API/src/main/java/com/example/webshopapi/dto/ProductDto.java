package com.example.webshopapi.dto;

import lombok.Data;

@Data
public class ProductDto {
    public String name;
    public int quantity;
    public double price;
}
