package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.config.result.TypedResult;
import com.example.webshopapi.dto.CouponDto;
import com.example.webshopapi.dto.requestObjects.CreateCouponRequest;

import java.util.List;

public interface CouponService {
    ExecutionResult createCoupon(CreateCouponRequest request);
    TypedResult<List<CouponDto>> getAllCoupons();
}