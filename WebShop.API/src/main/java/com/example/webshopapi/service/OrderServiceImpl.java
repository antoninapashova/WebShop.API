package com.example.webshopapi.service;

import com.example.webshopapi.entity.OrderEntity;
import com.example.webshopapi.entity.OrderItem;
import com.example.webshopapi.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    @Override
    public void createOrder(UUID userId) {
        var cart = cartRepository.getCartEntityByUserId(userId);
        if (cart == null) throw new NullPointerException("Cart not found");

        var user = userRepository.findById(userId).orElse(null);
        var order = new OrderEntity();
        order.setUser(user);
        orderRepository.save(order);

        cart.getItems().forEach(cartItem -> {
            var orderItem = modelMapper.map(cartItem, OrderItem.class);
            orderItem.setProductId(cartItem.getProduct().getId());
            orderItem.setOrder(order);
            orderItemRepository.save(orderItem);
        });

        cartRepository.delete(cart);
    }
}
