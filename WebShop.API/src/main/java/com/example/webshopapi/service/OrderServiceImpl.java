package com.example.webshopapi.service;

import com.example.webshopapi.config.DateTimeExtension;
import com.example.webshopapi.dto.OrderDto;
import com.example.webshopapi.entity.OrderEntity;
import com.example.webshopapi.entity.OrderItem;
import com.example.webshopapi.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy");

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
        order.setOrderDate(LocalDateTime.now());
        order.setDeliveryDate(DateTimeExtension.AddBusinessDays(LocalDateTime.now(), 3));
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
