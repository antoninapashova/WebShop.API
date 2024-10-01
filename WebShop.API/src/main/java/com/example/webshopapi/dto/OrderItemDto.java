package com.example.webshopapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderItemDto {
    public String name;
    public int quantity;
    public double price;
}
