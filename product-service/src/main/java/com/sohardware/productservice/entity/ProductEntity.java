package com.sohardware.productservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
/* Bonne pratique : (t_ pour table),  Distinguer les Tables des autres objets SQL(vue v_, index idx_, etc)
    et éviter les conflits avec les "Mots Réservés" SQL: USER deviens t_user, order t_order, group t_group, etc
 */
@Table(name = "t_products")
@Getter // Lombok Génère les getters
@Setter // Lombok Génère les setters
@NoArgsConstructor // Constructeur requis par JPA pour fonctionner en arrière-plan
// @AllArgsConstructor sur une entité JPA est considérée comme une mauvaise pratique
public class ProductEntity {

    /*
    - l'UUID (Universally Unique Identifier). Une chaîne de 36 caractères totalement imprévisible
    - pourquoi? : Si vos ID de produits sont séquentiels, un concurrent peut aller sur votre site,
      regarder l'ID du dernier produit créé (ex: http://sohardware.com) et savoir exactement combien d'articles vous avez dans votre catalogue.
      Pire, pour les Commandes, cela expose directement votre volume de ventes.
     - Spring Boot 4 / Hibernate 6 : utilisent UUIDv7 6 -> naturellement triés par ordre chronologique de création
 */

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Standard entreprise : Hibernate génère et injecte l'UUID automatiquement au format String lors du .save()
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;


    public ProductEntity(String name, String description, BigDecimal price, int stockQuantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}
