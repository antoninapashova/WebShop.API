package com.example.webshopapi.dto;

import lombok.Data;

@Data
public class ProductDto {
    public String id;
    public String name;
    public int quantity;
    public double price;
    public String description;
    public String categoryName;
}
