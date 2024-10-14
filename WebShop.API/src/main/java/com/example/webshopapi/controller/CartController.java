package com.example.webshopapi.controller;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.config.result.FailureType;
import com.example.webshopapi.dto.CartDto;
import com.example.webshopapi.dto.requestObjects.ChangeCartItemQuantityRequest;
import com.example.webshopapi.dto.requestObjects.SetCartItemQuantityRequest;
import com.example.webshopapi.entity.UserPrinciple;
import com.example.webshopapi.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add-to-cart/{productId}")
    public ResponseEntity<?> addProductToCart(@PathVariable String productId, @AuthenticationPrincipal UserPrinciple user) throws Exception {
        if (productId == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        ExecutionResult result = cartService.addProductToCart(UUID.fromString(productId), user.getUserId());
        if(result.getFailureType() == FailureType.NOT_FOUND){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result.getMessage());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/get-cart")
    public ResponseEntity<CartDto> getCart(@AuthenticationPrincipal UserPrinciple user) {
        CartDto cart = cartService.getCartByUserId(user.getUserId());

        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }

    @PutMapping("/cart/changeItemQuantity")
    public ResponseEntity<?> increaseCartItemQuantity(@RequestBody ChangeCartItemQuantityRequest changeQuantity) {
        if (changeQuantity == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        cartService.changeItemQuantity(changeQuantity);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("/cart/setItemQuantity")
    public ResponseEntity<?> setCartItemQuantity(@RequestBody SetCartItemQuantityRequest setQuantity) {
        if (setQuantity == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        cartService.setCartItemQuantity(setQuantity.cartItemId, setQuantity.quantity);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
