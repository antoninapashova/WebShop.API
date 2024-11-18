package com.example.webshopapi.controller;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.config.result.FailureType;
import com.example.webshopapi.config.result.TypedResult;
import com.example.webshopapi.dto.CouponDto;
import com.example.webshopapi.dto.requestObjects.CreateCouponRequest;
import com.example.webshopapi.entity.CouponEntity;
import com.example.webshopapi.service.CouponService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/create-coupon")
    public ResponseEntity<ExecutionResult> createCoupon(@RequestBody CreateCouponRequest request) throws IllegalArgumentException {
        ExecutionResult result = couponService.createCoupon(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/all-coupons")
    public ResponseEntity<ExecutionResult> getAllCoupons() {
        TypedResult<List<CouponDto>> result = couponService.getAllCoupons();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/coupon/{code}")
    public ResponseEntity<ExecutionResult> getCouponByCode(@PathVariable String code) {
        TypedResult<CouponEntity> result = couponService.getCouponByCode(code);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}