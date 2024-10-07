package com.example.webshopapi.config.result;

import lombok.Getter;

@Getter
public class ExecutionResult {
    private final boolean success;
    private final FailureType failureType;
    private final String message;

    public ExecutionResult() {
        this.success = true;
        this.failureType = null;
        this.message = null;
    }

    public ExecutionResult(String message) {
        this.success=true;
        this.failureType = null;
        this.message = message;
    }

    public ExecutionResult(FailureType failureType, String errorMessage) {
        this.success = false;
        this.failureType = failureType;
        this.message = errorMessage;
    }

    public static ExecutionResult success() {
        return new ExecutionResult();
    }

    public static ExecutionResult failure(FailureType failureType, String errorMessage) {
        return new ExecutionResult(failureType, errorMessage);
    }
}