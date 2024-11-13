package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;

public interface SubscriberService {
    ExecutionResult subscribe(String email);
}
