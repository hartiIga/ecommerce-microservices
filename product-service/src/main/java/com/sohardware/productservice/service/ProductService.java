package com.sohardware.productservice.service;

import com.sohardware.productservice.dto.ProductResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    // On simule une liste de composants informatiques
    private final List<ProductResponse> products = List.of(
            new ProductResponse("1", "NVIDIA RTX 5090", "La carte graphique ultime pour le gaming et l'IA", new BigDecimal("2499.99"), 15),
            new ProductResponse("2", "AMD Ryzen 9 9950X", "Processeur 16 cœurs ultra-puissant", new BigDecimal("649.00"), 42),
            new ProductResponse("3", "Corsair Dominator 64GB DDR5", "Kit mémoire haute performance 6000MHz", new BigDecimal("289.50"), 110)
    );

    public List<ProductResponse> getAllProducts() {
        return products;
    }
}
