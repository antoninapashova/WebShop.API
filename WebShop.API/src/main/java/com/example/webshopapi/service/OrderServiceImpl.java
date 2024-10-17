package com.example.webshopapi.service;

import com.example.webshopapi.config.DateTimeExtension;
import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.config.result.FailureType;
import com.example.webshopapi.config.result.TypedResult;
import com.example.webshopapi.dto.OrderDto;
import com.example.webshopapi.dto.OrderItemDto;
import com.example.webshopapi.dto.requestObjects.CreateOrderDto;
import com.example.webshopapi.entity.OrderEntity;
import com.example.webshopapi.entity.OrderItem;
import com.example.webshopapi.entity.ProductEntity;
import com.example.webshopapi.entity.enums.OrderStatus;
import com.example.webshopapi.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    public ExecutionResult createOrder(CreateOrderDto dto) {
        var cart = cartRepository.getCartEntityByUserId(dto.getUserId());
        if (cart == null) return new ExecutionResult(FailureType.NOT_FOUND, "Cart not found!");

        var user = userRepository.findById(dto.getUserId());
        if (user.isEmpty()) {
            return new ExecutionResult(FailureType.NOT_FOUND, "User not found!");
        }

        var order = new OrderEntity();
        order.setUser(user.get());
        order.setOrderDate(LocalDateTime.now());
        order.setDeliveryDate(DateTimeExtension.AddBusinessDays(LocalDateTime.now(), 3));
        order.setAddress(dto.getAddress());
        order.setOrderStatus(OrderStatus.Pending);
        order.setOrderDescription(dto.getDescription());
        orderRepository.save(order);

        cart.getItems().forEach(cartItem -> {
            var orderItem = modelMapper.map(cartItem, OrderItem.class);
            orderItem.setProductId(cartItem.getProduct().getId());
            orderItem.setOrder(order);
            orderItemRepository.save(orderItem);
        });

        cartRepository.delete(cart);
        return new ExecutionResult("Order send successfully");
    }

    @Override
    public List<OrderDto> retrieveAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> {
                    OrderDto orderDto = asDto(order);
                    orderDto.setOrderDate(order.getOrderDate().format(CUSTOM_FORMATTER));
                    orderDto.setDeliveryDate(order.getDeliveryDate().format(CUSTOM_FORMATTER));
                    if (order.getIsApproved() != null) {
                        if (order.getIsApproved()) {
                            orderDto.setIsApproved("Approved");
                        } else {
                            orderDto.setIsApproved("Rejected");
                        }
                    }
                    var user = order.getUser();
                    orderDto.setClientName(user.getFirstName() + " " + user.getLastName());
                    double totalAmount = order.getOrderItems().stream()
                            .mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();

                    orderDto.setTotalAmount(totalAmount);
                    return orderDto;
                }).toList();
    }

    @Override
    public ExecutionResult setOrderStatus(UUID orderId, boolean isApproved) {
        OrderEntity order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return new ExecutionResult(FailureType.NOT_FOUND, "Order not found");

        if (isApproved) {
            completeOrder(order);
        }

        if (order.getOrderItems().isEmpty()) {
            order.setIsApproved(false);
        } else {
            order.setIsApproved(isApproved);
        }

        OrderEntity save = orderRepository.save(order);
        String status;
        if (save.getIsApproved()) {
            status = "APPROVED";
        } else {
            status = "REJECTED";
        }

        return new ExecutionResult("Status is " + status + " successfully");
    }

    @Override
    public ExecutionResult changeOrderStatus(UUID orderId, String status) {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isEmpty()) {
            return new TypedResult<>(FailureType.NOT_FOUND, "Order not found!");
        }

        OrderEntity order = optionalOrder.get();
        if (Objects.equals(status, "Shipped")) {
            order.setOrderStatus(OrderStatus.Shipped);
        } else if (Objects.equals(status, "Delivered")) {
            order.setOrderStatus(OrderStatus.Delivered);
        }

        orderRepository.save(order);
        return new ExecutionResult("Status changed successfully!");
    }

    @Override
    public TypedResult<List<OrderItemDto>> getOrderItems(UUID orderId) {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId);
        if (orderItems.isEmpty()) {
            return new TypedResult<>(FailureType.NOT_FOUND, "No order items found for this order");
        }

        List<OrderItemDto> dtos = orderItems.stream().map(this::asOrderItemDto).toList();

        return new TypedResult<>(dtos);
    }

    private void completeOrder(OrderEntity order) {
        order.getOrderItems().forEach(orderItem -> {
            ProductEntity product = productRepository.findById(orderItem.getProductId()).orElse(null);
            boolean isProductUnavailable = product == null || product.isDeleted() || product.getQuantity() == 0;

            updateOrderItems(orderItem, product, isProductUnavailable);

            if (!isProductUnavailable) {
                decreaseProductsQuantity(orderItem, product);
            }
        });
    }

    private void updateOrderItems(OrderItem orderItem, ProductEntity product, boolean isProductUnavailable) {
        if (isProductUnavailable) {
            orderItemRepository.delete(orderItem);
            return;
        }

        boolean isProductQuantityInsufficient = orderItem.getQuantity() > product.getQuantity();
        if (isProductQuantityInsufficient) {
            updateOrderItemQuantity(orderItem, product);
        }
    }

    private void updateOrderItemQuantity(OrderItem orderItem, ProductEntity product) {
        orderItem.setQuantity(product.getQuantity());
        orderItemRepository.save(orderItem);
    }

    private void decreaseProductsQuantity(OrderItem orderItem, ProductEntity product) {
        int quanity = product.getQuantity();
        quanity -= orderItem.getQuantity();
        product.setQuantity(quanity);
        productRepository.save(product);
    }

    private OrderDto asDto(OrderEntity orderEntity) {
        return modelMapper.map(orderEntity, OrderDto.class);
    }

    private OrderItemDto asOrderItemDto(OrderItem orderItem) {
        return modelMapper.map(orderItem, OrderItemDto.class);
    }
}