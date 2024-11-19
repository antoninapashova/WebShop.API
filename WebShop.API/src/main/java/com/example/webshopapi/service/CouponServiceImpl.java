package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.CouponDto;
import com.example.webshopapi.dto.EmailBodyDto;
import com.example.webshopapi.dto.requestObjects.CreateCouponRequest;
import com.example.webshopapi.entity.CouponEntity;
import com.example.webshopapi.error.exception.CouponExpiredException;
import com.example.webshopapi.events.SendEmailEvent;
import com.example.webshopapi.repository.CouponRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher publisher;

    @Transactional
    @Override
    public ExecutionResult createCoupon(CreateCouponRequest request) {
        if (couponRepository.existsByCode(request.getCode())) {
            throw new EntityExistsException("Coupon code already exists!");
        }

        CouponEntity coupon = modelMapper.map(request, CouponEntity.class);
        couponRepository.save(coupon);

        publisher.publishEvent(new SendEmailEvent(this, "New promo code!", new EmailBodyDto(coupon.getCode(), coupon.getExpirationDate())));
        return new ExecutionResult("Coupon created successfully!");
    }

    @Override
    public List<CouponDto> getAllCoupons() {
        return couponRepository.findAll().stream().map(this::asDto).toList();
    }

    @Override
    public CouponEntity getCouponByCode(String code) {
        CouponEntity coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Coupon with this code not found!"));

        boolean isExpired = couponIsExpired(coupon);
        if (isExpired) {
            throw new CouponExpiredException("Coupon with code " + code + " has expired!");
        }

        return coupon;
    }

    private boolean couponIsExpired(CouponEntity coupon) {
        Date currentDate = new Date();
        Date expirationDate = coupon.getExpirationDate();

        return expirationDate != null && currentDate.after(expirationDate);
    }

    private CouponDto asDto(CouponEntity entity) {
        return modelMapper.map(entity, CouponDto.class);
    }
}