package com.example.webshopapi.service;

import com.example.webshopapi.dto.CartDto;
import com.example.webshopapi.dto.requestObjects.ChangeCartItemQuantityRequest;
import com.example.webshopapi.entity.CartEntity;
import com.example.webshopapi.entity.CartItemEntity;
import com.example.webshopapi.entity.ProductEntity;
import com.example.webshopapi.repository.CartItemRepository;
import com.example.webshopapi.repository.CartRepository;
import com.example.webshopapi.repository.ProductRepository;
import com.example.webshopapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Override
    public void addProductToCart(UUID productId, UUID userId) throws Exception {
        CartEntity cart = ensureUserHasCart(userId);
        if (cart == null) throw new Exception("Cart not found");

        assignItemToCartAsync(cart, productId);
    }

    @Override
    public CartDto getCartByUserId(UUID userId) {
        CartEntity cart = cartRepository.findByUserId(userId);
        var cartEntityDto = modelMapper.map(cart, CartDto.class);
        cartEntityDto.setTotalPrice(cart.getItems().stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum());

        return cartEntityDto;
    }

    @Override
    public void setCartItemQuantity(String cartItemId, int quantity) {
        var item = findItemById(cartItemId);

        if(quantity<=0){
            cartItemRepository.delete(item);
        }else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }

    @Override
    public void changeItemQuantity(ChangeCartItemQuantityRequest changeCartItemQuantity) {
       var item = findItemById(changeCartItemQuantity.cartItemId);

        if(changeCartItemQuantity.isIncreaseChange){
            item.setQuantity(item.getQuantity() + 1);
        }else {
            item.setQuantity(item.getQuantity() - 1);
        }

        if(item.getQuantity() == 0) {
            cartItemRepository.delete(item);
        }else{
            cartItemRepository.save(item);
        }
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
        boolean hasCart = cartRepository.existsByUserId(userId);
        if (!hasCart) return createUserCart(userId);
        return getUserCartAsync(userId);
    }

    private CartEntity createUserCart(UUID userId) {
        var user = userRepository.getUserEntityById(userId);
        CartEntity cart = new CartEntity(user, new ArrayList<>());
        return cartRepository.save(cart);
    }

    private CartEntity getUserCartAsync(UUID userId) {
        return cartRepository.findByUserId(userId);
    }

    private CartItemEntity findItemById(String itemId) {
        var item = cartItemRepository.findById(UUID.fromString(itemId)).orElse(null);
        if (item == null) throw new NullPointerException("Item not found for id " + itemId);
        return item;
    }
}