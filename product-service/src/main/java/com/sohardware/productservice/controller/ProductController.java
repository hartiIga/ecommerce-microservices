package com.sohardware.productservice.controller;

import com.sohardware.productservice.dto.ProductResponse;
import com.sohardware.productservice.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")// Bonne pratique : on versionne l'API (v1)
public class ProductController {

    private final ProductService productService;

    // Injection par constructeur : norme d'entreprise acceptée pour la testabilité
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }
}
