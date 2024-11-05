package com.example.webshopapi.service;

import com.example.webshopapi.config.DateTimeExtension;
import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.config.result.FailureType;
import com.example.webshopapi.config.result.TypedResult;
import com.example.webshopapi.dto.OrderDto;
import com.example.webshopapi.dto.OrderItemDto;
import com.example.webshopapi.dto.requestObjects.CreateOrderDto;
import com.example.webshopapi.entity.*;
import com.example.webshopapi.entity.enums.OrderStatus;
import com.example.webshopapi.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private final CouponRepository couponRepository;

    @Transactional
    @Override
    public ExecutionResult createOrder(CreateOrderDto dto) {
        CartEntity cart = cartRepository.getCartEntityByUserId(dto.getUserId());
        if (cart == null) return new ExecutionResult(FailureType.NOT_FOUND, "Cart not found!");

        UserEntity user = userRepository.findById(dto.getUserId()).orElse(null);
        if (user == null) {
            return new ExecutionResult(FailureType.NOT_FOUND, "User not found!");
        }

        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setDeliveryDate(DateTimeExtension.AddBusinessDays(LocalDateTime.now(), 3));
        order.setAddress(dto.getAddress());
        order.setOrderStatus(OrderStatus.Pending);
        order.setOrderDescription(dto.getDescription());

        if(dto.getCouponCode() != null) {
            CouponEntity coupon = couponRepository.findByCode(dto.getCouponCode()).orElse(null);

            if (coupon == null) {
                return new ExecutionResult(FailureType.NOT_FOUND, "Coupon with code " + dto.getCouponCode() + " not found!");
            }

            order.setCouponEntity(coupon);
        }

        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProduct().getId());
            orderItem.setName(cartItem.getProduct().getName());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            return orderItem;
        }).toList();

        order.setOrderItems(orderItems);
        orderRepository.save(order);
        cartRepository.delete(cart);

        return new ExecutionResult("Order send successfully!");
    }

    @Override
    public List<OrderDto> retrieveAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> {
                    OrderDto orderDto = asDto(order);
                    if (order.getIsApproved() != null) {
                        if (order.getIsApproved()) {
                            orderDto.setIsApproved("Approved");
                        } else {
                            orderDto.setIsApproved("Rejected");
                        }
                    }

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

        boolean orderHasItems = !order.getOrderItems().isEmpty();
        order.setIsApproved(orderHasItems);

        orderRepository.save(order);
        String status = orderHasItems ? "APPROVED" : "REJECTED";
        return new ExecutionResult(status);
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

        List<OrderItemDto> items = orderItems.stream().map(this::asOrderItemDto).toList();

        return new TypedResult<>(items);
    }

    @Override
    public TypedResult<OrderDto> getOrderById(UUID orderId) {
        OrderEntity entity = this.orderRepository.findById(orderId).orElse(null);

        if (entity == null) {
            return new TypedResult<>(FailureType.NOT_FOUND, "Order not found!");
        }

        return new TypedResult<>(asDto(entity));
    }

    private void completeOrder(OrderEntity order) {
        List<OrderItem> orderItems = new ArrayList<>(order.getOrderItems()); // Ensure you work with a fresh copy

        orderItems.forEach(orderItem -> {
            ProductEntity product = productRepository.findById(orderItem.getProductId()).orElse(null);
            boolean isProductUnavailable = product == null || product.isDeleted() || product.getQuantity() == 0;

            updateOrderItems(order, orderItem, product, isProductUnavailable);

            if (!isProductUnavailable) {
                decreaseProductsQuantity(orderItem, product);
            }
        });
    }

    private void updateOrderItems(OrderEntity order, OrderItem orderItem, ProductEntity product, boolean isProductUnavailable) {
        if (isProductUnavailable) {
            orderItemRepository.delete(orderItem);
            order.getOrderItems().remove(orderItem);

        } else {
            boolean isProductQuantityInsufficient = orderItem.getQuantity() > product.getQuantity();
            if (isProductQuantityInsufficient) {
                updateOrderItemQuantity(orderItem, product);
            }
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
        OrderDto orderDto = modelMapper.map(orderEntity, OrderDto.class);
        orderDto.setOrderDate(orderEntity.getOrderDate().format(CUSTOM_FORMATTER));
        orderDto.setDeliveryDate(orderEntity.getDeliveryDate().format(CUSTOM_FORMATTER));
        UserEntity user = orderEntity.getUser();
        orderDto.setClientName(user.getFirstName() + " " + user.getLastName());

        double totalAmount = orderEntity.getOrderItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();

        if (orderEntity.getCouponEntity() != null) {
            totalAmount -= totalAmount * orderEntity.getCouponEntity().getDiscount() / 100.0;
        }

        orderDto.setTotalAmount(totalAmount);

        return orderDto;
    }

    private OrderItemDto asOrderItemDto(OrderItem orderItem) {
        return modelMapper.map(orderItem, OrderItemDto.class);
    }
}