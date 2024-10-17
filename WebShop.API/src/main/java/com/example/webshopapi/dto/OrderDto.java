package com.example.webshopapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class OrderDto {
    public UUID id;
    public String orderDate;
    public String deliveryDate;
    public String clientName;
    public String isApproved;
    public double totalAmount;
    public String description;
    public String address;
    public String status;
}
