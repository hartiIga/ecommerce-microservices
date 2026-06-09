package com.sohardware.orderservice.service;

import com.sohardware.orderservice.dto.OrderRequest;
import com.sohardware.orderservice.entity.OrderEntity;
import com.sohardware.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class) // Active le moteur Mockito pour JUnit 5
@Profile("test")
public class OrderServiceTest {

    @Mock // Demande à Mockito de créer une fausse copie du Repository
    private OrderRepository orderRepository;

    @InjectMocks // Crée le vrai ProductService et y injecte automatiquement le faux repository ci-dessus
    private OrderService orderService;

    @Test
    @DisplayName("Devrait enregistrer la commande avec succès lorsque les données sont valides (Happy Path) ")
    public void shouldPlaceOrderSuccessfully() {
        //pattern Triple-A (Arrange, Act, Assert)

        // 1. Arrangement
        OrderRequest request = new OrderRequest("product-uuid-123", new BigDecimal("2499.99"), 2);

        // 2. ACT
        String result = orderService.placeOrder(request); //cree la commande

        // 3. ASSERT
        assertThat(result).contains("Commande SO-ORD-");

        // On vérifie que la méthode .save() du repository a bien été appelée exactement 1 fois
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    @DisplayName("Devrait lever une exception lorsque la quantité est inférieure ou égale à zéro (Edge Case)")
    public void shouldThrowExceptionWhenQuantityIsInvalid() {
        //pattern Triple-A (Arrange, Act, Assert)

        // 1. Arrangement
        OrderRequest invalidRequest = new OrderRequest("product-uuid-123", new BigDecimal("2499.99"), 0);

        // 2. ACT & ASSERT (Combinés avec AssertJ)
        assertThatThrownBy(() -> orderService.placeOrder(invalidRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La quantité doit être supérieure à zéro.");

    }
}
