package com.sohardware.productservice.service;


import com.sohardware.productservice.dto.ProductResponse;
import com.sohardware.productservice.entity.ProductEntity;
import com.sohardware.productservice.entity.ProductRepository;
import com.sohardware.productservice.mapper.ProductMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

// Active le moteur Mockito pour JUnit 5, permet à Mockito de gérer automatiquement les annotations @Mock et @InjectMocks
@ExtendWith(MockitoExtension.class)//WARNING : Spring n'est pas démarré, donc les dépendances ne s'injectent pas toutes seules.
@ActiveProfiles("test")
public class ProductServiceTest {

    @Mock // Demande à Mockito de créer une fausse copie (doublure) du Repository
    private ProductRepository productRepository;

    @InjectMocks // Crée le vrai ProductService et y injecte automatiquement le faux repository ci-dessus
    private ProductService productService;

    @Spy//crée une vraie instance du mapper et on demande à Mockito de la surveiller/l'injecter
    private ProductMapper productMapper = Mappers.getMapper(ProductMapper.class); // toujours l'initialiser lors de sa déclaration, ici Mappers.getMapper founir par mapStruc, sinon utiliser contructeur

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
        assertThat(result.getFirst().id()).isEqualTo("uuid-test-123");
        assertThat(result.getFirst().name()).isEqualTo("RTX 5090");

        /*
            >>WARNING<<
             Si .isEqualTo() est utilisé sur un BigDecimal, le test peut échouer si l'un est configuré avec 2499.99 et l'autre avec 2499.9900
            (problème d'échelle/scale).
            L'outil AssertJ possède isEqualByComparingTo spécifiquement pour comparer uniquement la valeur mathématique pure
         */
        assertThat(result.getFirst().price()).isEqualByComparingTo(new BigDecimal("2499.99"));
    }

    @Test
    @DisplayName("Devrait retourner la liste de tous les produits transformés en DTO paginé")
    void shouldReturnAllProductsWithPagination() {
        //pattern Triple-A (Arrange, Act, Assert)

        // 1. ARRANGEMENT (On prépare les fausses données que la BDD est censée renvoyer)
        ProductEntity mockEntity = new ProductEntity("RTX 5090", "GPU", new BigDecimal("2499.99"), 10);
        mockEntity.setId("uuid-test-123");

        //pagination: fausse implémentation de Page via Spring Data
        org.springframework.data.domain.Page<ProductEntity> mockPage = new org.springframework.data.domain.PageImpl<>(List.of(mockEntity));

        //any pour dire que la valeur exacte n'a pas d'importance, seul l'objet compte
        when(productRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(mockPage);

        // 2. ACT (On demande la page 0 avec une taille de 10)
        org.springframework.data.domain.Page<ProductResponse> result = productService.getAllProducts(0, 10);

        // 3. ASSERT (On vérifie avec AssertJ que le résultat est conforme aux exigences)
        assertThat(result).hasSize(1);  // Le contenu de la page a 1 élément
        assertThat(result.getContent().getFirst().name()).isEqualTo("RTX 5090");
        assertThat(result.getTotalElements()).isEqualTo(1); // La métadonnée globale est exacte
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
