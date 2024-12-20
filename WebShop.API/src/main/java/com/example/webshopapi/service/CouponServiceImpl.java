package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.CouponDto;
import com.example.webshopapi.dto.requestObjects.CreateCouponRequest;
import com.example.webshopapi.entity.CouponEntity;
import com.example.webshopapi.error.exception.CouponExpiredException;
import com.example.webshopapi.events.SendEmailEvent;
import com.example.webshopapi.repository.CouponRepository;
import com.example.webshopapi.service.email.TemplateEnum;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CouponServiceImpl implements CouponService {
    CouponRepository couponRepository;
    ModelMapper modelMapper;
    ApplicationEventPublisher publisher;

    @Transactional
    @Override
    public ExecutionResult createCoupon(CreateCouponRequest request) {
        if (couponRepository.existsByCode(request.getCode())) {
            throw new EntityExistsException("Coupon code already exists!");
        }

        CouponEntity coupon = modelMapper.map(request, CouponEntity.class);
        couponRepository.save(coupon);

        Map<String, Object> templateModel = Map.of(
                "promoCode", coupon.getCode(),
                "expirationDate", coupon.getExpirationDate()
        );

        publisher.publishEvent(new SendEmailEvent(this, "New promo code!", templateModel, TemplateEnum.PROMO_CODE));
        return new ExecutionResult("Coupon created successfully!");
    }

    @Override
    public List<CouponDto> getAllCoupons() {
        return couponRepository.findAll().stream().map(this::asDto).toList();
    }

    @Override
    public CouponEntity getCouponByCode(String code) {
        CouponEntity coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Coupon not found!"));

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