package com.example.webshopapi.dto.requestObjects;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateOrderRequest {
    @NotBlank(message = "Id cannot be blank!")
    private UUID userId;

    @NotBlank(message = "Address cannot be blank!")
    private String address;

    @NotBlank(message = "Description cannot be blank!")
    private String description;

    private String couponCode;
}
