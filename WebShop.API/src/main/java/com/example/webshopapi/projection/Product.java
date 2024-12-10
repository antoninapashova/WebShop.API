package com.example.webshopapi.projection;

import java.time.LocalDateTime;
import java.util.UUID;

public interface Product {
     UUID getProductId();
     String getProductName();
     UUID getCategoryId();
     String getDescription();
     double getPrice();
     Double getPriceInPromotion();
     Boolean getIsActive();
     LocalDateTime getEndDate();
     byte[][] getImages();
}