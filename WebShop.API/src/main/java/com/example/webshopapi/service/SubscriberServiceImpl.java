package com.example.webshopapi.service;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.config.result.FailureType;
import com.example.webshopapi.entity.SubscriberEntity;
import com.example.webshopapi.repository.SubscriberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SubscriberServiceImpl implements SubscriberService {

    private final SubscriberRepository subscriberRepository;

    @Override
    public ExecutionResult subscribe(String email) {
        Optional<SubscriberEntity> subscriber = subscriberRepository.findByEmail(email);

        if(subscriber.isPresent()){
            return new ExecutionResult(FailureType.UNKNOWN, "You already have subscribed to our newsletter!");
        }

        SubscriberEntity entity = new SubscriberEntity();
        entity.setEmail(email);
        subscriberRepository.save(entity);

        return new ExecutionResult("Successfully subscribed!");
    }
}
