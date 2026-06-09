package com.sohardware.orderservice.service;

import com.sohardware.orderservice.dto.OrderRequest;
import com.sohardware.orderservice.entity.OrderEntity;
import com.sohardware.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional // Garantit qu'en cas de crash, la base de données fait un rollback propre
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public String placeOrder(OrderRequest orderRequest) {
        // Validation basique des règles métiers (Cas limite pour nos futurs tests !)
        if (orderRequest.quantity() <= 0) {
            throw new IllegalArgumentException("La quantité doit être supérieure à zéro.");
        }

        // Génération d'un numéro de commande d'entreprise unique
        String orderNumber = "SO-ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        OrderEntity order = new OrderEntity(
                orderNumber,
                orderRequest.productId(),
                orderRequest.price(),
                orderRequest.quantity()
        );

        orderRepository.save(order);

        return "Commande " + orderNumber + " enregistrée avec succès !";
    }


}
