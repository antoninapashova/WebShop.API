package com.example.webshopapi.dto.requestObjects;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateProductRequest {
    @NotBlank(message = "Name cannot be blank!")
    public String name;

    @Min(value = 1)
    public int quantity;

    @Min(value = 1)
    public double price;

    @NotBlank(message = "Name cannot be blank!")
    @Size(min = 10)
    public String description;

    @NotBlank(message = "Category cannot be blank!")
    public String categoryId;

    @NotEmpty(message = "You need to provide at least one photo for this product!")
    public MultipartFile[] images;
}