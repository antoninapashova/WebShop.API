package com.example.webshopapi.service;

import com.example.webshopapi.entity.CartEntity;
import com.example.webshopapi.entity.CartItemEntity;
import com.example.webshopapi.entity.ProductEntity;
import com.example.webshopapi.entity.UserEntity;
import com.example.webshopapi.repository.CartItemRepository;
import com.example.webshopapi.repository.CartRepository;
import com.example.webshopapi.repository.ProductRepository;
import com.example.webshopapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Override
    public void addProductToCart(UUID productId, UUID userId) throws Exception {
        CartEntity cart = ensureUserHasCart(userId);
        if (cart == null) throw new Exception("Cart not found");

        assignItemToCartAsync(cart, productId);
    }

    private void assignItemToCartAsync(CartEntity cart, UUID productId) {
        ProductEntity productEntity = productRepository.findProductEntityById(productId);
        if (productEntity == null) throw new NullPointerException("Product does not exist!");

        CartItemEntity cartItem = cart.getItems().stream().filter(x -> x.getProduct().getId() == productEntity.getId()).findAny().orElse(null);

        if (cartItem == null) {
            cartItem = new CartItemEntity(productEntity.getName(), 1, productEntity.getPrice(), cart, productEntity);
            cartItemRepository.save(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItemRepository.save(cartItem);
        }
    }

    private CartEntity ensureUserHasCart(UUID userId) {
        boolean hasCart = cartRepository.existsByUserID(userId);
        if (!hasCart) return createUserCart(userId);
        return getUserCartAsync(userId);
    }

    private CartEntity createUserCart(UUID userId) {
       CartEntity cart = new CartEntity(userId, new ArrayList<>());
       setCartToUser(userId, cart);
       return cartRepository.save(cart);
    }

    private CartEntity getUserCartAsync(UUID userId) {
        return cartRepository.findByUserID(userId);
    }

    private void setCartToUser(UUID userId, CartEntity cart) {
        UserEntity userEntity = userRepository.getUserEntityById(userId);
        if(userEntity == null) throw new NullPointerException("User does not exist!");
        userEntity.setCart(cart);
        userRepository.save(userEntity);
    }
}
