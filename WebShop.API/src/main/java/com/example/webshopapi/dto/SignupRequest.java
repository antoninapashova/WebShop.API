package com.example.webshopapi.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
    private String name;
    private String firstName;
    private String lastName;
}
