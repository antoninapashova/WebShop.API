package com.example.webshopapi.config.result;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Getter
public class ExecutionResult {
    private FailureType failureType;
    private Date timestamp;
    private String details;
    private String message;

    public ExecutionResult(String message) {
        this.message = message;
    }

    public ExecutionResult(FailureType failureType, String errorMessage) {
        this.failureType = failureType;
        this.message = errorMessage;
    }

    public ExecutionResult(Date date, String message, String description) {
        this.timestamp = date;
        this.message = message;
        this.details = description;
    }
}