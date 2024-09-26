package com.example.webshopapi.service;

import com.example.webshopapi.dto.CartDto;

import java.util.UUID;

public interface CartService {
    void addProductToCart(UUID productId, UUID userId) throws Exception;
    CartDto getCartByUserId(UUID userId);
}
