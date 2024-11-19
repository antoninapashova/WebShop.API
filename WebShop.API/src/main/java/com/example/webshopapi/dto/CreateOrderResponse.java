package com.example.webshopapi.dto;

import lombok.Data;

@Data
public class CreateOrderResponse {
    public String id;

    public CreateOrderResponse(String id) {
        this.id = id;
    }
}