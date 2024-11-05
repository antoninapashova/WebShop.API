package com.example.webshopapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserOrderDto {
    public String orderDate;
    public double totalAmount;
    public String address;
    public String status;
    public List<UserOrderItemDto> items;
}
