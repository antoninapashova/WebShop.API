package com.example.webshopapi.service.email;

public enum TemplateEnum {
    PROMO_CODE("promo-code.html"),
    ORDER_DETAILS("order-details.html");

    public final String label;

    TemplateEnum(String label) {
        this.label = label;
    }
}