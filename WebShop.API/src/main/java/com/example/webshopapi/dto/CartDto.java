package com.example.webshopapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class CartDto {
    public List<CartItemDto> cartItems;
    public double totalPrice;
}
