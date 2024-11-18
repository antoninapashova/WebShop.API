package com.example.webshopapi.error.exception;

public class ExistingCouponException extends RuntimeException{
    public ExistingCouponException(String message){
        super(message);
    }
}