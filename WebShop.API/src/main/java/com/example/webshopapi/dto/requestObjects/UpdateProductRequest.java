package com.example.webshopapi.dto.requestObjects;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProductRequest {
    public String name;
    public int quantity;
    public double price;
    public String description;
    public String categoryId;
    private MultipartFile[] images;
}
