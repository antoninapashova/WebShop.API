package com.example.webshopapi.dto.requestObjects;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class CreateCouponRequest {
    @NotBlank(message = "Name cannot be blank!")
    private String name;

    @NotBlank(message = "Code cannot be blank!")
    @Size(min = 3, max = 10, message = "Code should be between 3 and 15 characters!")
    private String code;

    @Min(value = 1, message = "Discount should be more than 1%")
    private Long discount;

    @FutureOrPresent
    private Date expirationDate;
}