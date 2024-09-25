package com.example.webshopapi.service;

import java.util.UUID;

public interface CartService {
    void addProductToCart(UUID productId, UUID userId) throws Exception;
}
