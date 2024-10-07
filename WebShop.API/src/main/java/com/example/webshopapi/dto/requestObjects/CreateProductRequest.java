package com.example.webshopapi.dto.requestObjects;

import lombok.Data;

@Data
public class CreateProductRequest {
    public String name;
    public int quantity;
    public double price;
    public String categoryId;
}