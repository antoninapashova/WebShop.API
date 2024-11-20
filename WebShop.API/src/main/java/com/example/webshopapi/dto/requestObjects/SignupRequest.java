package com.example.webshopapi.dto.requestObjects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank(message = "Username cannot be blank!")
    @Size(min = 3, max = 15, message = "Username should be between 3 and 15 characters!")
    private String username;

    @NotBlank(message = "First name cannot be blank!")
    @Size(min = 3, max = 15, message = "First name should be between 3 and 15 characters!")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank!")
    @Size(min = 3, max = 15, message = "Last name should be between 3 and 15 characters!")
    private String lastName;

    @NotBlank(message = "Email cannot be blank!")
    @Email(message = "Email should be valid", regexp = "^[A-Za-z0-9+_.-]+@(.+)$\", ^[A-Za-z0-9+_.-]")
    private String email;

    @NotBlank(message = "Password cannot be blank!")
    @Size(min = 3, max = 15, message = "Password should be between 3 and 15 characters!")
    private String password;
}