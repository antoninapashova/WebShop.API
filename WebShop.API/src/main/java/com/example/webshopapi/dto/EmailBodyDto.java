package com.example.webshopapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class EmailBodyDto {
    public String promoCode;
    public Date expirationDate;
}
