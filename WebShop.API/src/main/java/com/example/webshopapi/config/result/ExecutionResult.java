package com.example.webshopapi.config.result;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Getter
public class ExecutionResult {
    private Date timestamp;
    private String details;
    private String message;

    public ExecutionResult(String message) {
        this.message = message;
    }

    public ExecutionResult(Date date, String message, String description) {
        this.timestamp = date;
        this.message = message;
        this.details = description;
    }
}