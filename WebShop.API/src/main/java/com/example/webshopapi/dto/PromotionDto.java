package com.example.webshopapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class PromotionDto {
    private double discount;
    private String startDate;
    private String endDate;
    private List<String> productsInPromotion;
}
