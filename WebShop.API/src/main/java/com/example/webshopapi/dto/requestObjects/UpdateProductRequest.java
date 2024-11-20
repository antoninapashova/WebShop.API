package com.example.webshopapi.dto.requestObjects;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProductRequest {
    @NotBlank(message = "Name cannot be blank!")
    public String name;

    @Min(value = 1, message = "Quantity must be at least 1")
    public int quantity;

    @Min(value = 1, message = "Price must be at least 1")
    public double price;

    @NotBlank(message = "Description cannot be blank!")
    @Size(min = 10, max = 200)
    public String description;

    @NotBlank(message = "Category cannot be blank!")
    public String categoryId;

    @NotEmpty(message = "You need to provide at least one photo for this product!")
    public MultipartFile[] images;
}
