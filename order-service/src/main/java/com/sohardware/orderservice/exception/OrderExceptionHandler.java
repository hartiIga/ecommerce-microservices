package com.sohardware.orderservice.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class OrderExceptionHandler {

    // 1. Votre gestionnaire actuel pour la validation des DTO (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> erreurs = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((erreur) -> {
            String nomChamp = ((FieldError) erreur).getField();
            String messageErreur = erreur.getDefaultMessage();
            erreurs.put(nomChamp, messageErreur);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erreurs);
    }

    // 2. Gestion des violations de contraintes de contraintes d'intégrité
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrity(DataIntegrityViolationException ex) {
        // On renvoie un statut 409 Conflict ou 400 Bad Request selon le besoin
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Une erreur de contrainte de données est survenue ");
    }

    // 3. Exemple de gestion pour une exception métier personnalisée qui devrait extend RunetimeException
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
//        return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .body(ex.getMessage());
//    }

    // 4. Le "Filet de sécurité" pour toutes les autres erreurs inconnues (Evite le crash brut)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllUncaughtExceptions(Exception ex) {
        // En production, il est crucial de logguer l'erreur ici pour les développeurs
        // log.error("Erreur interne non gérée", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Une erreur interne est survenue. Veuillez contacter le support.");
    }


}
