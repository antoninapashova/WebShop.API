package com.example.webshopapi.controller;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.CartDto;
import com.example.webshopapi.dto.requestObjects.ChangeCartItemQuantityRequest;
import com.example.webshopapi.dto.requestObjects.SetCartItemQuantityRequest;
import com.example.webshopapi.entity.UserPrinciple;
import com.example.webshopapi.service.CartService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;

    @PostMapping("/add-to-cart/{productId}")
    public ResponseEntity<ExecutionResult> addProductToCart(@PathVariable String productId, @AuthenticationPrincipal UserPrinciple user) throws Exception {
        ExecutionResult result = cartService.addProductToCart(UUID.fromString(productId), user.getUserId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/get-cart")
    public ResponseEntity<CartDto> getCart(@AuthenticationPrincipal UserPrinciple user) {
        CartDto result = cartService.getCartByUserId(user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/cart/changeItemQuantity")
    public ResponseEntity<?> changeItemQuantity(@RequestBody ChangeCartItemQuantityRequest changeQuantity) {
        ExecutionResult result = cartService.changeItemQuantity(changeQuantity);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/cart/setItemQuantity")
    public ResponseEntity<?> setCartItemQuantity(@RequestBody SetCartItemQuantityRequest setQuantity) {
        cartService.setCartItemQuantity(setQuantity.cartItemId, setQuantity.quantity);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable String itemId) {
        ExecutionResult result = cartService.deleteItem(UUID.fromString(itemId));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
