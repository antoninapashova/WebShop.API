package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.CouponDto;
import com.example.webshopapi.dto.requestObjects.CreateCouponRequest;
import com.example.webshopapi.entity.CouponEntity;

import java.util.List;

public interface CouponService {
    ExecutionResult createCoupon(CreateCouponRequest request);
    List<CouponDto> getAllCoupons();
    CouponEntity getCouponByCode(String code);
}