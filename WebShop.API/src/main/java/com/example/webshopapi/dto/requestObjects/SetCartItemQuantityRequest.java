package com.example.webshopapi.dto.requestObjects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class SetCartItemQuantityRequest {
    @NotBlank(message = "Id cannot be blank!")
    public String cartItemId;

    @Positive(message = "Quantity cannot be negative!")
    public int quantity;
}