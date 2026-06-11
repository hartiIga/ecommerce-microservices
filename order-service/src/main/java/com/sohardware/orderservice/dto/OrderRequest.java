package com.sohardware.orderservice.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record OrderRequest(

        @NotBlank(message = "Le code de référence est obligatoire")
//        @Pattern(regexp = "^CMD-\\d{5}$", message = "La référence doit suivre le format 'CMD-XXXXX' (ex: CMD-12345)")
        String productId,

        @NotNull(message = "Le montant total est obligatoire")
        @PositiveOrZero(message = "Le montant total ne peut pas être négatif")
        @Digits(integer = 6, fraction = 2, message = "Le format du montant est invalide (max 6 chiffres avant la virgule, 2 après)")
        BigDecimal  price,

        @Min(value = 1, message = "La quantité doit être supérieure à zéro.")
        int quantity

//        @NotNull(message = "La date de livraison estimée est obligatoire")
//        @Future(message = "La date de livraison doit être une date future")
//        LocalDateTime dateLivraisonEstimee
) {
}
