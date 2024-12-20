package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.entity.SubscriberEntity;
import com.example.webshopapi.repository.SubscriberRepository;
import jakarta.persistence.EntityExistsException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriberServiceImpl implements SubscriberService {
   SubscriberRepository subscriberRepository;

    @Override
    public ExecutionResult subscribe(String email) {
        SubscriberEntity subscriber = subscriberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityExistsException("You already have subscribed to our newsletter!"));

        subscriber.setEmail(email);
        subscriberRepository.save(subscriber);

        return new ExecutionResult("Successfully subscribed!");
    }
}
