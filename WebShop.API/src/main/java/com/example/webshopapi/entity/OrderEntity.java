package com.example.webshopapi.entity;

import com.example.webshopapi.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity extends BaseEntity {
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private Boolean isApproved;
    private String orderDescription;
    private String address;
    private String payment;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, mappedBy = "order", orphanRemoval = true)
    private List<OrderItem> orderItems;
}
