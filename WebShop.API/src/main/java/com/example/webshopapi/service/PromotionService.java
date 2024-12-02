package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.PromotionDto;

import java.text.ParseException;

public interface PromotionService {
   ExecutionResult createPromotion(PromotionDto promotionDto) throws ParseException;
}
