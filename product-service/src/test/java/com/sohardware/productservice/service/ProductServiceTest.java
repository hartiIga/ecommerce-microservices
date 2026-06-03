package com.sohardware.productservice.service;


import com.sohardware.productservice.dto.ProductResponse;
import com.sohardware.productservice.entity.ProductEntity;
import com.sohardware.productservice.entity.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Active le moteur Mockito pour JUnit 5, permet à Mockito de gérer automatiquement les annotations @Mock et @InjectMocks
public class ProductServiceTest {

    @Mock // Demande à Mockito de créer une fausse copie (doublure) du Repository
    private ProductRepository productRepository;

    @InjectMocks // Crée le vrai ProductService et y injecte automatiquement le faux repository ci-dessus
    private ProductService productService;

    @Test
    @DisplayName("Devrait retourner la liste de tous les produits transformés en DTO")
    void shouldReturnAllProducts() {
        //pattern Triple-A (Arrange, Act, Assert)

        // 1. ARRANGEMENT (On prépare les fausses données que la BDD est censée renvoyer)
        ProductEntity mockEntity = new ProductEntity("RTX 5090", "GPU", new BigDecimal("2499.99"), 10);
        mockEntity.setId("uuid-test-123");

        // On dicte le comportement du Mock : "Quand le service appellera findAll(), renvoie cette liste"
        when(productRepository.findAll()).thenReturn(List.of(mockEntity));

        // 2. ACT (On exécute la vraie méthode de notre service)
        List<ProductResponse> result = productService.getAllProducts();

        // 3. ASSERT (On vérifie avec AssertJ que le résultat est conforme aux exigences)
        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo("uuid-test-123");
        assertThat(result.get(0).name()).isEqualTo("RTX 5090");

        /*
            >>WARNING<<
             Si .isEqualTo() est utilisé sur un BigDecimal, le test peut échouer si l'un est configuré avec 2499.99 et l'autre avec 2499.9900
            (problème d'échelle/scale).
            L'outil AssertJ possède isEqualByComparingTo spécifiquement pour comparer uniquement la valeur mathématique pure
         */
        assertThat(result.get(0).price()).isEqualByComparingTo(new BigDecimal("2499.99"));
    }

    @Test
    @DisplayName("Devrait lever une exception quand le produit est introuvable par son ID")
    void shouldThrowExceptionWhenProductNotFound() {
        // 1. ARRANGEMENT
        String fakeId = "id-inexistant";

        // On simule le fait que la base de données renvoie un Optional VIDE
        when(productRepository.findById(fakeId)).thenReturn(java.util.Optional.empty());

        // 2. ACT & ASSERT (Combinés avec AssertJ)
        // On vérifie que l'exécution de la méthode déclenche bien la bonne exception
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> productService.getProductById(fakeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Le composant informatique avec l'ID " + fakeId + " est introuvable.");
    }
}
