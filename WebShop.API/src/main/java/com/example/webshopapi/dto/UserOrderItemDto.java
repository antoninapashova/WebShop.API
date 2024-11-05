package com.example.webshopapi.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserOrderItemDto {
    public UUID id;
    public String name;
    public int quantity;
    public double price;
    public byte[] img;
}
