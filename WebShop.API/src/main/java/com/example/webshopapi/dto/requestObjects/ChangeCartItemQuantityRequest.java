package com.example.webshopapi.dto.requestObjects;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeCartItemQuantityRequest {
    @NotBlank(message = "Id cannot be blank!")
    public String cartItemId;

    @NotBlank(message = "Change type cannot be blank!")
    public boolean isIncreaseChange;
}