package com.example.JavaSprint5_1BlackJack.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(WebExchangeBindException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));

        var error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed: " + details,
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleJsonError(HttpMessageNotReadableException ex) {
        var error = new ApiErrorResponse(400, "Invalid JSON format or type mismatch", LocalDateTime.now());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        var error = new ApiErrorResponse(400, ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.badRequest().body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAll(Exception ex, ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();

        // Check if the request is for Swagger or API docs
        if (path.contains("swagger") || path.contains("v3/api-docs")) {
            // Log it as debug and let Spring handle it
            log.debug("Swagger resource requested, bypassing GlobalExceptionHandler: {}", path);
            throw new RuntimeException(ex); // Re-throw to allow default handling
        }

        log.error("Unhandled exception caught: ", ex);
        var error = new ApiErrorResponse(500, "Internal error", LocalDateTime.now());
        return ResponseEntity.internalServerError().body(error);
    }

    // 2. Handle 404 Not Found (Custom Exception)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        var error = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalState(IllegalStateException ex) {
        var error = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(), // 或者 400
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}
