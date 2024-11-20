package com.example.webshopapi.error;

import java.util.HashMap;
import java.util.Map;

public class ErrorMapper {
    public static Map<String, String> createErrorMap(String errorMessage) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", errorMessage);
        return errorResponse;
    }
}