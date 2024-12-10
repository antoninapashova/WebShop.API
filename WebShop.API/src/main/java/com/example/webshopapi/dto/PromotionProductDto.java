package com.example.webshopapi.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PromotionProductDto {
    public UUID productId;
    public String productName;
    public double price;
    public double priceInPromotion;
    public String description;
    public String categoryName;
    public Boolean isActive;
    public List<byte[]> images;
    public String endDate;
}
