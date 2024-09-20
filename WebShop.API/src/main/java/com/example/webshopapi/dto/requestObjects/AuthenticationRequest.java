package com.example.webshopapi.dto.requestObjects;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}