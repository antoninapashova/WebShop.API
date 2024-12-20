package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.CartDto;
import com.example.webshopapi.dto.CartItemDto;
import com.example.webshopapi.dto.requestObjects.ChangeCartItemQuantityRequest;
import com.example.webshopapi.entity.CartEntity;
import com.example.webshopapi.entity.CartItemEntity;
import com.example.webshopapi.entity.ProductEntity;
import com.example.webshopapi.entity.UserEntity;
import com.example.webshopapi.repository.CartItemRepository;
import com.example.webshopapi.repository.CartRepository;
import com.example.webshopapi.repository.ProductRepository;
import com.example.webshopapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;
    ProductRepository productRepository;
    CartItemRepository cartItemRepository;
    UserRepository userRepository;
    ModelMapper modelMapper;

    @Override
    public ExecutionResult addProductToCart(UUID productId, UUID userId) {
        CartEntity cart = ensureUserHasCart(userId);
        if (cart == null) throw new EntityNotFoundException("Cart not found!");
        return assignItemToCartAsync(cart, productId);
    }

    @Override
    public CartDto getCartByUserId(UUID userId) {
        CartEntity cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            throw new EntityNotFoundException("User has no assigned cart!");
        }

        CartDto cartEntityDto = modelMapper.map(cart, CartDto.class);
        cartEntityDto.setTotalPrice(cart.getItems().stream().mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity()).sum());
        cartEntityDto.setCartItems(cart.getItems().stream().map(this::asCartItemDto).toList());

        return cartEntityDto;
    }

    @Override
    public void setCartItemQuantity(String cartItemId, int quantity) {
        CartItemEntity item = findItemById(cartItemId);

        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }

    @Override
    public ExecutionResult changeItemQuantity(ChangeCartItemQuantityRequest changeCartItemQuantity) {
        CartItemEntity item = findItemById(changeCartItemQuantity.cartItemId);

        ExecutionResult result;

        if (changeCartItemQuantity.isIncreaseChange) {
            item.setQuantity(item.getQuantity() + 1);
            result = new ExecutionResult("Quantity increased successfully!");
        } else {
            item.setQuantity(item.getQuantity() - 1);
            result = new ExecutionResult("Quantity decreased successfully!");
        }

        if (item.getQuantity() == 0) {
            cartItemRepository.delete(item);
            result = new ExecutionResult("Item is removed from cart successfully!");
        } else {
            cartItemRepository.save(item);
        }

        return result;
    }

    @Override
    public ExecutionResult deleteItem(UUID itemId) {
        boolean isExists = cartItemRepository.existsById(itemId);
        if (!isExists) throw new EntityNotFoundException("This item does not exists!");
        cartItemRepository.deleteById(itemId);
        return new ExecutionResult("Item removed successfully!");
    }

    private ExecutionResult assignItemToCartAsync(CartEntity cart, UUID productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found!"));

        CartItemEntity cartItem = cart.getItems().stream()
                .filter(x -> x.getProduct().getId() == productEntity.getId())
                .findFirst()
                .orElse(null);

        ExecutionResult result;

        if (cartItem == null) {
            cartItem = new CartItemEntity(1, productEntity);
            cart.getItems().add(cartItem);
            result = new ExecutionResult("Product added successfully");
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cart.getItems().add(cartItem);
            result = new ExecutionResult("Product quantity increased successfully");
        }

        cartRepository.save(cart);

        return result;
    }

    private CartEntity ensureUserHasCart(UUID userId) {
        boolean hasCart = cartRepository.existsByUserId(userId);
        if (!hasCart) return createUserCart(userId);
        return getUserCartAsync(userId);
    }

    private CartEntity createUserCart(UUID userId) {
        UserEntity user = userRepository.getUserEntityById(userId);
        CartEntity cart = new CartEntity(user, new ArrayList<>());
        return cartRepository.save(cart);
    }

    private CartEntity getUserCartAsync(UUID userId) {
        return cartRepository.findByUserId(userId);
    }

    private CartItemEntity findItemById(String itemId) {
        return cartItemRepository.findById(UUID.fromString(itemId))
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found!"));
    }

    private CartItemDto asCartItemDto(CartItemEntity entity) {
        CartItemDto dto = modelMapper.map(entity, CartItemDto.class);

        dto.setPrice(productRepository.findPromotionalPrice(entity.getProduct().getId()) != null
                ? productRepository.findPromotionalPrice(entity.getProduct().getId())
                : entity.getProduct().getPrice());

        dto.setName(entity.getProduct().getName());
        dto.setImg(entity.getProduct().getImages().getFirst().getImg());

        return dto;
    }
}