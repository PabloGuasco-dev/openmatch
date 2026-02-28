package com.openmatch.banco.exception;

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
 * Manejo global de excepciones. Patrón Controller Advice.
 * Devuelve respuestas HTTP coherentes para errores de negocio y validación.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BancoNotFoundException.class)
    public ResponseEntity<ErrorBody> handleBancoNotFound(BancoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorBody(HttpStatus.NOT_FOUND.value(), ex.getMessage(), Instant.now())
        );
    }

    @ExceptionHandler(DuplicateBancoException.class)
    public ResponseEntity<ErrorBody> handleDuplicateBanco(DuplicateBancoException ex) {
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
        body.put("message", "Error de validación: " + errors);
        body.put("timestamp", Instant.now());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorBody> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorBody(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error interno del servidor", Instant.now())
        );
    }

    public record ErrorBody(int status, String message, Instant timestamp) {}
}
