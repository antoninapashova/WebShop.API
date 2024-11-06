package com.example.webshopapi.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateOrderDto {
    private UUID userId;
    private String address;
    private String description;
    private String couponCode;
}
