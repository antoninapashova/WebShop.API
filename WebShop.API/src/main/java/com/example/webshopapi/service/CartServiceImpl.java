package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.config.result.FailureType;
import com.example.webshopapi.config.result.TypedResult;
import com.example.webshopapi.dto.CartDto;
import com.example.webshopapi.dto.CartItemDto;
import com.example.webshopapi.dto.requestObjects.ChangeCartItemQuantityRequest;
import com.example.webshopapi.entity.CartEntity;
import com.example.webshopapi.entity.CartItemEntity;
import com.example.webshopapi.entity.ProductEntity;
import com.example.webshopapi.error.exception.CartItemNotFoundException;
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
    public ExecutionResult addProductToCart(UUID productId, UUID userId) throws Exception {
        CartEntity cart = ensureUserHasCart(userId);
        if (cart == null) return new ExecutionResult(FailureType.NOT_FOUND, "No cart found");

        return assignItemToCartAsync(cart, productId);
    }

    @Override
    public TypedResult<CartDto> getCartByUserId(UUID userId) {
        CartEntity cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            return new TypedResult<>(FailureType.NOT_FOUND, "User has no assigned cart!");
        }

        var cartEntityDto = modelMapper.map(cart, CartDto.class);
        cartEntityDto.setTotalPrice(cart.getItems().stream().mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity()).sum());
        cartEntityDto.setCartItems(cart.getItems().stream().map(this::asCartItemDto).toList());
        return new TypedResult<>(cartEntityDto);
    }

    @Override
    public void setCartItemQuantity(String cartItemId, int quantity) {
        TypedResult<CartItemEntity> result = findItemById(cartItemId);
        CartItemEntity item = result.getData();

        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }

    @Override
    public ExecutionResult changeItemQuantity(ChangeCartItemQuantityRequest changeCartItemQuantity) {
        TypedResult<CartItemEntity> itemResult = findItemById(changeCartItemQuantity.cartItemId);

        CartItemEntity item = itemResult.getData();
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

        if (!isExists) {
            return new ExecutionResult(FailureType.NOT_FOUND, "Item not found!");
        }

        cartItemRepository.deleteById(itemId);
        return new ExecutionResult("Item removed successfully!");
    }

    private ExecutionResult assignItemToCartAsync(CartEntity cart, UUID productId) {
        ProductEntity productEntity = productRepository.findProductEntityById(productId);
        if (productEntity == null) return new ExecutionResult(FailureType.NOT_FOUND, "Product does not exist!");

        CartItemEntity cartItem = cart.getItems().stream().filter(x -> x.getProduct().getId() == productEntity.getId()).findAny().orElse(null);
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
        var user = userRepository.getUserEntityById(userId);
        CartEntity cart = new CartEntity(user, new ArrayList<>());
        return cartRepository.save(cart);
    }

    private CartEntity getUserCartAsync(UUID userId) {
        return cartRepository.findByUserId(userId);
    }

    private TypedResult<CartItemEntity> findItemById(String itemId) {
        CartItemEntity item = cartItemRepository.findById(UUID.fromString(itemId))
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found!"));

        return new TypedResult<>(item);
    }

    private CartItemDto asCartItemDto(CartItemEntity entity) {
        CartItemDto dto = modelMapper.map(entity, CartItemDto.class);
        dto.setPrice(entity.getProduct().getPrice());
        dto.setName(entity.getProduct().getName());
        dto.setImg(entity.getProduct().getImages().getFirst().getImg());
        return dto;
    }
}