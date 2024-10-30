package com.example.webshopapi.dto.requestObjects;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateProductRequest {
    public String name;
    public int quantity;
    public double price;
    public String description;
    public String categoryId;
    public MultipartFile[] images;
}