package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.CartDto;
import com.example.webshopapi.dto.requestObjects.ChangeCartItemQuantityRequest;

import java.util.UUID;

public interface CartService {
    ExecutionResult addProductToCart(UUID productId, UUID userId) throws Exception;
    CartDto getCartByUserId(UUID userId);
    void setCartItemQuantity(String cartItemId, int quantity);
    void changeItemQuantity(ChangeCartItemQuantityRequest increaseQuantity);
}
