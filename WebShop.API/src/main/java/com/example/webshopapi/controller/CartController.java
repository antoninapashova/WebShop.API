package com.example.webshopapi.controller;

import com.example.webshopapi.dto.CartDto;
import com.example.webshopapi.entity.UserPrinciple;
import com.example.webshopapi.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add-to-cart/{productId}")
    public ResponseEntity<?> addProductToCart(@PathVariable String productId, @AuthenticationPrincipal UserPrinciple user) throws Exception {
        if (productId == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        cartService.addProductToCart(UUID.fromString(productId), user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/get-cart")
    public ResponseEntity<CartDto> getCart(@AuthenticationPrincipal UserPrinciple user) throws Exception {
        CartDto cart = cartService.getCartByUserId(user.getUserId());

        if(cart==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }
}
