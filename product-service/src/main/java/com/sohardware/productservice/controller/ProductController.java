package com.sohardware.productservice.controller;

import com.sohardware.productservice.dto.ProductResponse;
import com.sohardware.productservice.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/products", "/api/v1/products/"})// Bonne pratique : on versionne l'API (v1)
public class ProductController {

    private final ProductService productService;

    // Injection par constructeur : norme d'entreprise acceptée pour la testabilité
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping
    public Page<ProductResponse> getAllProductsPaginated(
            @RequestParam(name = "page", defaultValue = "0") int page,   // Page 0 par défaut (la première)
            @RequestParam(name = "size", defaultValue = "10") int size   // 10 éléments par page par défaut
    ) {
        return productService.getAllProducts(page, size);
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }
}
