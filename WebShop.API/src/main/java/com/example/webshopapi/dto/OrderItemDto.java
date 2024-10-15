package com.example.webshopapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class OrderItemDto {
    public UUID id;
    public String name;
    public int quantity;
    public double price;
}
