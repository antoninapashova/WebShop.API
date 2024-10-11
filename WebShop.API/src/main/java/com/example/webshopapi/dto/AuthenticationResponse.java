package com.example.webshopapi.dto;

import lombok.Data;

@Data
public class AuthenticationResponse {
    public String token;
    public String role;
}
