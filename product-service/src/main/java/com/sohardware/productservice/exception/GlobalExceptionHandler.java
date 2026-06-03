package com.sohardware.productservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice // Dit à Spring d'intercepter les exceptions de tous les contrôleurs
public class GlobalExceptionHandler {

    // On attrape spécifiquement les IllegalArgumentException (produit introuvable)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(IllegalArgumentException ex) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(), // Statut HTTP 404 (Plus propre qu'une 500 !)
                "Resource Not Found",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
