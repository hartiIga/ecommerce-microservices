package com.sohardware.orderservice;

import com.sohardware.orderservice.dto.OrderRequest;
import com.sohardware.orderservice.repository.OrderRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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


//test d'intégration global (end-to-end). L'application démarre sur un vrai port réseau local aléatoire.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class OrderServiceApplicationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("Devrait enregistrer une commande en BDD via un appel HTTP POST")
    public void shouldPlaceOrderSuccesfullyOverHttp() {
        // 1. Arrange : On prépare la requête d'achat du client
        OrderRequest orderRequest = new OrderRequest("product-uuid-999", new BigDecimal("2499.99"), 1);
        String url = "http://localhost:" + port + "/api/v1/orders";

        // 2. ACT : On effectue le véritable appel réseau HTTP POST
        ResponseEntity<String> response = restTemplate.postForEntity(url, orderRequest, String.class);

        // 3.ASSERT: On valide le statut 201 Created et la persistance physique
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("Commande SO-ORD-");

        // On vérifie que la base de données PostgreSQL éphémère contient bien notre commande
        assertThat(orderRepository.count()).isEqualTo(1);
        assertThat(orderRepository.findAll().getFirst().getProductId()).isEqualTo("product-uuid-999");
    }

    @Test
    @DisplayName("Devrait lever une exception lorsque la quantité est inférieure ou égale à zéro (Edge Case)")
    public void shouldThrowExceptionWhenQuantityIsInvalid() {
        //pattern Triple-A (Arrange, Act, Assert)

        // 1. Arrangement
        OrderRequest invalidRequest = new OrderRequest("product-uuid-123", new BigDecimal("2499.99"), 0);
        String url = "http://localhost:" + port + "/api/v1/orders";

        // 2. ACT : On effectue le véritable appel réseau HTTP POST
        ResponseEntity<String> response = restTemplate.postForEntity(url, invalidRequest, String.class);

        // 3. ASSERT (Combinés avec AssertJ)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("La quantité doit être supérieure à zéro.");
    }

}
