package com.example.webshopapi.repository;

import com.example.webshopapi.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, UUID> {
    boolean existsByCode(String code);
}