package com.example.webshopapi.error;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.error.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExecutionResult> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        ExecutionResult errorDetails = new ExecutionResult(new Date(), ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<ExecutionResult> handleInvalidUserException(InvalidUserException ex, WebRequest request) {
        ExecutionResult errorDetails = new ExecutionResult(new Date(), ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(ExistingCouponException.class)
    public ResponseEntity<?> handleExistingCouponException(ExistingCouponException ex, WebRequest request) {
        ExecutionResult errorDetails = new ExecutionResult(new Date(), ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ExecutionResult> handleCouponNotFoundException(CouponNotFoundException ex, WebRequest request) {
        ExecutionResult errorDetails = new ExecutionResult(new Date(), ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(CouponExpiredException.class)
    public ResponseEntity<ExecutionResult> handleCouponExpiredException(CouponExpiredException ex, WebRequest request) {
        ExecutionResult errorDetails = new ExecutionResult(new Date(), ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ExecutionResult> handleCartItemNotFoundException(CartItemNotFoundException ex, WebRequest request) {
        ExecutionResult errorDetails = new ExecutionResult(new Date(), ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }
}
