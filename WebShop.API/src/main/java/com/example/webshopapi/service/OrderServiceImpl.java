package com.example.webshopapi.service;

import com.example.webshopapi.config.DateTimeExtension;
import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.*;
import com.example.webshopapi.entity.*;
import com.example.webshopapi.entity.enums.OrderStatus;
import com.example.webshopapi.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    public CreateOrderResponse createOrder(CreateOrderDto dto) {
        CartEntity cart = cartRepository.getCartEntityByUserId(dto.getUserId());
        if (cart == null) throw new EntityNotFoundException("You don't have assigned cart!");

        UserEntity user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found!"));

        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setDeliveryDate(DateTimeExtension.AddBusinessDays(LocalDateTime.now(), 3));
        order.setAddress(dto.getAddress());
        order.setOrderStatus(OrderStatus.Pending);
        order.setOrderDescription(dto.getDescription());

        if (dto.getCouponCode() != null) {
            CouponEntity coupon = couponRepository.findByCode(dto.getCouponCode())
                    .orElseThrow(() -> new EntityNotFoundException("Coupon with code " + dto.getCouponCode() + " not found!"));

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

       return new CreateOrderResponse(order.getId().toString());
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
        OrderEntity order = findOrderById(orderId);

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
        OrderEntity order = findOrderById(orderId);

        if (Objects.equals(status, "Shipped")) {
            order.setOrderStatus(OrderStatus.Shipped);
        } else if (Objects.equals(status, "Delivered")) {
            order.setOrderStatus(OrderStatus.Delivered);
        }

        orderRepository.save(order);
        return new ExecutionResult("Status changed successfully!");
    }

    @Override
    public List<OrderItemDto> getOrderItems(UUID orderId) {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId);
        return orderItems.stream().map(this::asOrderItemDto).toList();
    }

    @Override
    public OrderDto getOrderById(UUID orderId) {
        OrderEntity order = findOrderById(orderId);
        return asDto(order);
    }

    @Override
    public List<UserOrderDto> getUserOrders(UUID userId) {
        return orderRepository.findAllByUserId(userId).stream()
                .map(order -> {
                    UserOrderDto userOrderDto = modelMapper.map(order, UserOrderDto.class);
                    List<UserOrderItemDto> items = order.getOrderItems()
                            .stream().map(this::asUserOrderItemDto).toList();
                    userOrderDto.setItems(items);
                    userOrderDto.setOrderDate(order.getOrderDate().format(CUSTOM_FORMATTER));

                    double totalAmount = order.getOrderItems().stream()
                            .mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();

                    userOrderDto.setTotalAmount(totalAmount);
                    return userOrderDto;
                }).toList();
    }

    @Override
    public AnalyticsResponse calculateAnalytics() {
        LocalDate currentDate = LocalDate.now();
        LocalDate previousMonthDate = currentDate.minusMonths(1);

        Long currentMonthOrders = getTotalOrdersForMonth(currentDate.getMonthValue(), currentDate.getYear());
        Long previousMonthOrders = getTotalOrdersForMonth(previousMonthDate.getMonthValue(), previousMonthDate.getYear());

        Long currentMonthEarning = getTotalEarningForMonth(currentDate.getMonthValue(), currentDate.getYear());
        Long previousMonthEarning = getTotalEarningForMonth(previousMonthDate.getMonthValue(), previousMonthDate.getYear());

        Long placed = orderRepository.countByOrderStatus(OrderStatus.Placed);
        Long shipped = orderRepository.countByOrderStatus(OrderStatus.Shipped);
        Long delivered = orderRepository.countByOrderStatus(OrderStatus.Delivered);

        return new AnalyticsResponse(placed, shipped, delivered, currentMonthOrders, previousMonthOrders, currentMonthEarning, previousMonthEarning);
    }

    private Long getTotalEarningForMonth(int monthValue, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthValue - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date startOfMonth = calendar.getTime();
        LocalDateTime startOfMonthLocal = startOfMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        Date endOfMonth = calendar.getTime();
        LocalDateTime endOfMonthLocal = endOfMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        List<OrderEntity> orders = orderRepository.findByOrderDateBetweenAndOrderStatus(startOfMonthLocal, endOfMonthLocal, OrderStatus.Delivered);

        long sum = 0L;

        for (OrderEntity order : orders) {
            long totalAmount = order.getOrderItems().stream()
                    .mapToLong(item -> (long) (item.getQuantity() * item.getPrice())).sum();
            sum += totalAmount;
        }

        return sum;
    }

    private Long getTotalOrdersForMonth(int monthValue, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthValue - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date startOfMonth = calendar.getTime();
        LocalDateTime startOfMonthLocal = startOfMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        Date endOfMonth = calendar.getTime();
        LocalDateTime endOfMonthLocal = endOfMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        List<OrderEntity> orders = orderRepository.findByOrderDateBetweenAndOrderStatus(startOfMonthLocal, endOfMonthLocal, OrderStatus.Delivered);

        return (long) orders.size();
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

    private OrderEntity findOrderById(UUID id){
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
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

    private UserOrderItemDto asUserOrderItemDto(OrderItem item) {
        ProductEntity product = this.productRepository.findProductEntityById(item.getProductId());
        UserOrderItemDto itemDto = modelMapper.map(item, UserOrderItemDto.class);
        itemDto.setImg(product.getImages().getFirst().getImg());
        return itemDto;
    }

    private OrderItemDto asOrderItemDto(OrderItem orderItem) {
        return modelMapper.map(orderItem, OrderItemDto.class);
    }
}