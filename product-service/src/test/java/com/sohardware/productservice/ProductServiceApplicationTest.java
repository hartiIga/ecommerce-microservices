package com.sohardware.productservice;

import com.sohardware.productservice.dto.ProductResponse;
import com.sohardware.productservice.entity.ProductEntity;
import com.sohardware.productservice.entity.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Démarre le microservice sur un port aléatoire pour le test
@Testcontainers // Active le support des conteneurs éphémères pour ce test
class ProductServiceApplicationTest {

    @Container
    @ServiceConnection  //Connecte automatiquement la source de données de l'application à ce conteneur Docker éphémère
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16-alpine");

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate; //Pour simuler des appels HTTP

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        // Avant chaque test, on nettoie la base de données éphémère pour garantir l'isolation des tests
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("Devrait insérer un produit en BDD et le récupérer via l'API REST")
    void shouldCreateAndFetchProduct() {
        // 1. ARRANGEMENT : On insère un composant réel directement en BDD
        ProductEntity rtxCard = new ProductEntity("NVIDIA RTX 5090", "GPU Next-Gen", new BigDecimal("2499.99"), 5);
        productRepository.save(rtxCard);

        // 2. ACT : Notre robot TestRestTemplate effectue une vraie requête HTTP GET sur notre API
        String url = "http://localhost:" + port + "/api/v1/products";
        ResponseEntity<ProductResponse[]> response = restTemplate.getForEntity(url, ProductResponse[].class);

        // 3. ASSERT : On vérifie que le code HTTP est 200 OK et que les données PostgreSQL sont exactes
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ProductResponse[] products = response.getBody();

        assertThat(products).isNotNull().hasSize(1);
        assertThat(products[0].name()).isEqualTo("NVIDIA RTX 5090");
        assertThat(products[0].price()).isEqualByComparingTo(new BigDecimal("2499.99"));
        assertThat(products[0].id()).isNotNull(); // On valide que l'UUIDv7 a bien été généré par la BDD
    }
}