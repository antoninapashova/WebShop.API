package com.example.webshopapi.service;

import com.example.webshopapi.dto.CartDto;
import com.example.webshopapi.dto.requestObjects.ChangeCartItemQuantityRequest;

import java.util.UUID;

public interface CartService {
    void addProductToCart(UUID productId, UUID userId) throws Exception;
    CartDto getCartByUserId(UUID userId);
    void setCartItemQuantity(String cartItemId, int quantity);
    void changeItemQuantity(ChangeCartItemQuantityRequest increaseQuantity);
}
