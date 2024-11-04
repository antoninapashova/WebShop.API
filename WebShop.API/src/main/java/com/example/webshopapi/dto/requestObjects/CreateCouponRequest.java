package com.example.webshopapi.dto.requestObjects;

import lombok.Data;

import java.util.Date;

@Data
public class CreateCouponRequest {
    private String name;
    private String code;
    private Long discount;
    private Date expirationDate;
}