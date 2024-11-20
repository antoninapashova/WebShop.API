package com.example.webshopapi.dto.requestObjects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticationRequest {
    @NotBlank(message = "Username cannot be blank!")
    @Size(min = 3, max = 15, message = "Username must be between 3 and 15 characters!")
    private String username;

    @NotBlank(message = "Password cannot be blank!")
    @Size(min = 3, max = 15, message = "Password should be between 3 and 15 characters!")
    private String password;
}