package com.example.webshopapi.dto.requestObjects;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SetOrderStatusRequest {
    @NotBlank(message = "Id cannot be blank!")
    public String orderId;

    @NotBlank(message = "Approval status cannot be blank!")
    public boolean isApproved;
}
