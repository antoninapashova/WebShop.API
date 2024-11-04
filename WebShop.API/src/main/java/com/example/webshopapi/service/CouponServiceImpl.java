package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.config.result.FailureType;
import com.example.webshopapi.config.result.TypedResult;
import com.example.webshopapi.dto.CouponDto;
import com.example.webshopapi.dto.requestObjects.CreateCouponRequest;
import com.example.webshopapi.entity.CouponEntity;
import com.example.webshopapi.repository.CouponRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;

    public CouponServiceImpl(CouponRepository couponRepository, ModelMapper modelMapper) {
        this.couponRepository = couponRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ExecutionResult createCoupon(CreateCouponRequest request) {

        if(couponRepository.existsByCode(request.getCode())){
            return new ExecutionResult(FailureType.UNKNOWN, "Coupon code already exists!");
        }

        CouponEntity coupon = modelMapper.map(request, CouponEntity.class);
        couponRepository.save(coupon);

        return new ExecutionResult("Coupon created successfully!");
    }

    @Override
    public TypedResult<List<CouponDto>> getAllCoupons() {
       List<CouponDto> dtos =  couponRepository.findAll().stream().map(this::asDto).toList();

       if(dtos.isEmpty()){
           return new TypedResult<>(FailureType.NOT_FOUND, "No coupons found!");
       }

       return new TypedResult<>(dtos);
    }

    private CouponDto asDto(CouponEntity entity){
        return modelMapper.map(entity, CouponDto.class);
    }
}