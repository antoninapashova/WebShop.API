package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.entity.SubscriberEntity;
import com.example.webshopapi.error.exception.InvalidSubscriptionException;
import com.example.webshopapi.repository.SubscriberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubscriberServiceImpl implements SubscriberService {
    private final SubscriberRepository subscriberRepository;

    @Override
    public ExecutionResult subscribe(String email) {
        SubscriberEntity subscriber = subscriberRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidSubscriptionException("You already have subscribed to our newsletter!"));

        subscriber.setEmail(email);
        subscriberRepository.save(subscriber);

        return new ExecutionResult("Successfully subscribed!");
    }
}
