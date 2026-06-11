package com.sohardware.orderservice.service;

import com.sohardware.orderservice.OrderMapper;
import com.sohardware.orderservice.dto.OrderRequest;
import com.sohardware.orderservice.dto.OrderResponse;
import com.sohardware.orderservice.entity.OrderEntity;
import com.sohardware.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j // Génère automatiquement la variable "log" utilisable dans la classe
@Transactional // Garantit qu'en cas de crash, la base de données fait un rollback propre
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(entity -> new OrderResponse(
                entity.getOrderNumber(),
                entity.getProductId(),
                entity.getPrice(),
                entity.getQuantity(),
                entity.getCreatedAt()
        )).toList();
    }

    public Page<OrderResponse> getAllOrdersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<OrderEntity> orderPage = orderRepository.findAll(pageable);

        return orderPage.map(entity -> new OrderResponse(
                entity.getOrderNumber(),
                entity.getProductId(),
                entity.getPrice(),
                entity.getQuantity(),
                entity.getCreatedAt()
        ));
    }

    public String placeOrder(OrderRequest orderRequest) {
        // Génération d'un numéro de commande d'entreprise unique
        String orderNumber = "SO-ORD-" + UUID.randomUUID().toString().toUpperCase();

        OrderEntity order = orderMapper.toEntity(orderRequest);
        order.setOrderNumber(orderNumber);

        orderRepository.save(order);

        return "Commande " + orderNumber + " enregistrée avec succès !";
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        // 1. Log de traçabilité (Niveau INFO ou DEBUG)
        log.info("Création d'une commande client: {}", orderRequest.productId());

        String orderNumber = "SO-ORD-" + UUID.randomUUID().toString().toUpperCase();

        OrderEntity orderToSave= orderMapper.toEntity(orderRequest);
        orderToSave.setOrderNumber(orderNumber);
        OrderEntity orderSaved = orderRepository.save(orderToSave);

        //2. Log de succès
        log.info("Commande créée avec succès. Numéro: {}", orderNumber);

        return orderMapper.toResponse(orderSaved);
    }

}
