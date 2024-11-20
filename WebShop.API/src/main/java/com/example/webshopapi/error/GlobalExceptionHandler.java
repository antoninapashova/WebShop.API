package com.example.webshopapi.error;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.error.exception.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExecutionResult> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        ExecutionResult errorDetails = new ExecutionResult(new Date(), ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> handleExistingCouponException(EntityExistsException ex, WebRequest request) {
        ExecutionResult errorDetails = new ExecutionResult(new Date(), ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDetails);
    }

    @ExceptionHandler(CouponExpiredException.class)
    public ResponseEntity<ExecutionResult> handleCouponExpiredException(CouponExpiredException ex, WebRequest request) {
        ExecutionResult errorDetails = new ExecutionResult(new Date(), ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder builder = new StringBuilder();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName;
            try {
                fieldName = ((FieldError) error).getField();

            } catch (ClassCastException ex) {
                fieldName = error.getObjectName();
            }

            String message = error.getDefaultMessage();
            builder.append(fieldName).append(": ").append(message).append("; ");
        });

        ExecutionResult errorDetails = new ExecutionResult(new Date(), builder.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
