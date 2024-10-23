package com.example.webshopapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDto {
    public String id;
    public String name;
    public int quantity;
    public double price;
    public String description;
    public String categoryName;
    public List<ImageDto> images;
}
