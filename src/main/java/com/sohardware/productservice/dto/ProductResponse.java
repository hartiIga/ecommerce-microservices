package com.sohardware.productservice.dto;

import java.math.BigDecimal;


//En entreprise, on n'utilise jamais le type double ou float pour de l'argent.
// Les double ont des erreurs d'arrondis binaires (par exemple, 0.1 + 0.2 peut donner 0.30000000000000004).
// Le type BigDecimal garantit une précision mathématique absolue, indispensable pour la facturation
public record ProductResponse(
        String id,
        String name,
        String description,
        BigDecimal price,
        int stockQuantity
) {}
