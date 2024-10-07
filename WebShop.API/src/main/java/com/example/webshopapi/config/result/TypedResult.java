package com.example.webshopapi.config.result;

import lombok.Getter;

@Getter
public class TypedResult<T> extends ExecutionResult {
    private final T data;
    
    public TypedResult(T data) {
        super();
        this.data = data;
    }

    public TypedResult(FailureType failureType, String errorMessage) {
        super(failureType, errorMessage);
        this.data = null;
    }

    public static <T> TypedResult<T> success(T data) {
        return new TypedResult<>(data);
    }

    public static <T> TypedResult<T> typedFailure(FailureType failureType, String errorMessage) {
        return new TypedResult<>(failureType, errorMessage);
    }
}
