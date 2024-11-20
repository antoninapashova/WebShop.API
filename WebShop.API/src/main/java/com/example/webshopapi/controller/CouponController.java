package com.example.webshopapi.controller;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.CouponDto;
import com.example.webshopapi.dto.requestObjects.CreateCouponRequest;
import com.example.webshopapi.entity.CouponEntity;
import com.example.webshopapi.service.CouponService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping("/create-coupon")
    public ResponseEntity<ExecutionResult> createCoupon(@Valid @RequestBody CreateCouponRequest request) throws IllegalArgumentException {
        ExecutionResult result = couponService.createCoupon(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/all-coupons")
    public ResponseEntity<List<CouponDto>> getAllCoupons() {
        List<CouponDto> result = couponService.getAllCoupons();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/coupon/{code}")
    public ResponseEntity<CouponEntity> getCouponByCode(@PathVariable String code) {
        CouponEntity result = couponService.getCouponByCode(code);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}