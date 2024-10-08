package com.example.webshopapi.service;

import com.example.webshopapi.config.DateTimeExtension;
import com.example.webshopapi.dto.OrderDto;
import com.example.webshopapi.entity.OrderEntity;
import com.example.webshopapi.entity.OrderItem;
import com.example.webshopapi.entity.ProductEntity;
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
    private final ProductRepository productRepository;

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

    @Override
    public List<OrderDto> retrieveAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> {
                    var mappedOrder = asDto(order);
                    mappedOrder.setOrderDate(order.getOrderDate().format(CUSTOM_FORMATTER));
                    mappedOrder.setDeliveryDate(order.getDeliveryDate().format(CUSTOM_FORMATTER));
                    var user = order.getUser();
                    mappedOrder.setClientName(user.getFirstName() + " " + user.getLastName());
                    double totalAmount = order.getOrderItems().stream()
                            .mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();

                    mappedOrder.setTotalAmount(totalAmount);
                    return mappedOrder;
                }).toList();
    }

    @Override
    public void setOrderStatus(UUID orderId, boolean isApproved) {
        OrderEntity order = orderRepository.findById(orderId).orElse(null);
        if(order == null) throw new NullPointerException("Order not found");

        if(isApproved){
            completeOrder(order);
        }

        if (order.getOrderItems().isEmpty())
        {
            order.setIsApproved(false);
        }else {
            order.setIsApproved(isApproved);
        }

        orderRepository.save(order);
    }

    private void completeOrder(OrderEntity order)
    {
        order.getOrderItems().forEach(orderItem -> {
            ProductEntity product = productRepository.findById(orderItem.getProductId()).orElse(null);
            boolean isProductUnavailable = product == null || product.isDeleted() || product.getQuantity()==0;

            updateOrderItems(orderItem, product, isProductUnavailable);

            if (!isProductUnavailable) {
                decreaseProductsQuantity(orderItem, product);
            }
        });
    }

    private void updateOrderItems(OrderItem orderItem, ProductEntity product, boolean isProductUnavailable)
    {
        if (isProductUnavailable)
        {
            orderItemRepository.deleteOrderItemById(orderItem.getId());
            return;
        }

        boolean isProductQuantityInsufficient = orderItem.getQuantity() > product.getQuantity();
        if (isProductQuantityInsufficient)
        {
            UpdateOrderItemQuantity(orderItem, product);
        }
    }

    private void UpdateOrderItemQuantity(OrderItem orderItem, ProductEntity product)
    {
        orderItem.setQuantity(product.getQuantity());
        orderItemRepository.save(orderItem);
    }

    private void decreaseProductsQuantity(OrderItem orderItem, ProductEntity product)
    {
        int quanity = product.getQuantity();
        quanity -= orderItem.getQuantity();
        product.setQuantity(quanity);
        productRepository.save(product);
    }

    private OrderDto asDto(OrderEntity orderEntity) {
        return modelMapper.map(orderEntity, OrderDto.class);
    }
}