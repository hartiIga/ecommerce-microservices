package com.sohardware.orderservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "t_orders")
@Getter
@Setter
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Hibernate gère l'UUIDv7 automatiquement
    private String id;

    private String orderNumber; // Numéro de commande commercial
    private String productId;
    private BigDecimal price;
    private int quantity;
    private LocalDateTime createdAt;

    public OrderEntity(){}

    public OrderEntity(String orderNumber, String productId, BigDecimal price, int quantity) {
        this.orderNumber = orderNumber;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = LocalDateTime.now(ZoneOffset.UTC);
    }
}
