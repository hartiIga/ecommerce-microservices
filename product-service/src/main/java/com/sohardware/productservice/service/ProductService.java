package com.sohardware.productservice.service;

import com.sohardware.productservice.dto.ProductResponse;
import com.sohardware.productservice.entity.ProductEntity;
import com.sohardware.productservice.entity.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // On simule une liste de composants informatiques
//    private final List<ProductResponse> products = List.of(
//            new ProductResponse("1", "NVIDIA RTX 5090", "La carte graphique ultime pour le gaming et l'IA", new BigDecimal("2499.99"), 15),
//            new ProductResponse("2", "AMD Ryzen 9 9950X", "Processeur 16 cœurs ultra-puissant", new BigDecimal("649.00"), 42),
//            new ProductResponse("3", "Corsair Dominator 64GB DDR5", "Kit mémoire haute performance 6000MHz", new BigDecimal("289.50"), 110)
//    );

    // @PostConstruct dit à Spring d'exécuter cette méthode UNE FOIS que le service est démarré
    //s'exécute plus tôt que CommandLineRunner qui lui s'exécute tout à la fin
    @PostConstruct
    public void initDatabase() {
        // Si la base PostgreSQL est vide, on insère notre catalogue de hardware initial
        if (productRepository.count() == 0) {
            productRepository.saveAll(List.of(
                    new ProductEntity("NVIDIA RTX 5090", "La carte graphique ultime pour le gaming et l'IA", new BigDecimal("2499.99"), 15),
                    new ProductEntity("AMD Ryzen 9 9950X", "Processeur 16 cœurs ultra-puissant", new BigDecimal("649.00"), 42),
                    new ProductEntity("Corsair Dominator 64GB DDR5", "Kit mémoire haute performance 6000MHz", new BigDecimal("289.50"), 110)
            ));
        }
    }

    public List<ProductResponse> getAllProducts() {
        // Design Pattern d'entreprise : On récupère les ENTITÉS de la BDD et on les transforme en DTO (Records)
        return productRepository.findAll()
                .stream()
                .map(entity -> new ProductResponse(
                        entity.getId(), // L'ID généré automatiquement par Hibernate
                        entity.getName(),
                        entity.getDescription(),
                        entity.getPrice(),
                        entity.getStockQuantity()
                ))
                .toList();
    }
}
