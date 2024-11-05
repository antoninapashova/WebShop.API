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
    public ResponseEntity<?> createCoupon(@RequestBody CreateCouponRequest request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body not found!");
        }

        ExecutionResult result = couponService.createCoupon(request);

        if (result.getFailureType() == FailureType.UNKNOWN) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/all-coupons")
    public ResponseEntity<?> getAllCoupons() {
        TypedResult<List<CouponDto>> result = couponService.getAllCoupons();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/coupon/{code}")
    public ResponseEntity<?> getCouponByCode(@PathVariable String code) {
        TypedResult<CouponEntity> result = couponService.getCouponByCode(code);

        if(result.getFailureType() == FailureType.UNKNOWN){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result);
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}