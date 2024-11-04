package com.example.webshopapi.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class CouponDto {
    private UUID id;
    private String name;
    private String code;
    private Long discount;
    private Date expirationDate;
}
