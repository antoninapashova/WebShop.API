package com.example.webshopapi.dto.requestObjects;

import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
    private String name;
    private String firstName;
    private String lastName;
}
