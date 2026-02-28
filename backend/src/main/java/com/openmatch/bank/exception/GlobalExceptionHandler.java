package com.openmatch.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handling. Controller Advice pattern.
 * Returns consistent HTTP responses for business and validation errors.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BankNotFoundException.class)
    public ResponseEntity<ErrorBody> handleBankNotFound(BankNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorBody(HttpStatus.NOT_FOUND.value(), ex.getMessage(), Instant.now())
        );
    }

    @ExceptionHandler(DuplicateBankException.class)
    public ResponseEntity<ErrorBody> handleDuplicateBank(DuplicateBankException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorBody(HttpStatus.CONFLICT.value(), ex.getMessage(), Instant.now())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", "Validation error: " + errors);
        body.put("timestamp", Instant.now());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorBody> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorBody(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error", Instant.now())
        );
    }

    public record ErrorBody(int status, String message, Instant timestamp) {}
}
