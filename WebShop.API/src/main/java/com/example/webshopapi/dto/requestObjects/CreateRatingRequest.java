package com.example.webshopapi.dto.requestObjects;

import lombok.Data;

@Data
public class CreateRatingRequest {
    public String productId;
    public int score;
}
