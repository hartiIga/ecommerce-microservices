package com.sohardware.productservice.cucumber;

import com.sohardware.productservice.dto.ProductResponse;
import com.sohardware.productservice.entity.ProductEntity;
import com.sohardware.productservice.entity.ProductRepository;
import io.cucumber.java.PendingException;
import io.cucumber.java.fr.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

//classe de liaison géré par la bibliothèque cucumber-spring et non spring
//Cucumber va analyser toutes les classes qui contiennent des annotations de phrases (comme @Quand ou @Alors...
//puis prend le contrôle de ces classes les enregistre lui-même comme des Beans temporaires à l'intérieur du moteur Spring
public class CatalogSteps {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    private ResponseEntity<String> paginationResponse;

    private ResponseEntity<ProductResponse[]> response;

    // LIAISON PHRASE 1
    @Étantdonnéque("le catalogue contient un produit {string} au prix de {bigdecimal}")
    public void leCatalogueContientUnProduitAuPrixDe(String name, BigDecimal price) {
        // On nettoie la BDD avant et on insère le produit décrit en français
        productRepository.deleteAll();

        ProductEntity entity = new ProductEntity(name, "Composant via Cucumber", price, 10);
        productRepository.save(entity);
    }

    // LIAISON PHRASE 2
    @Quand("le client demande à voir tous les produits du catalogue")
    public void leClientDemandeAVoirTousLesProduitsDuCatalogue() {
        String url = "http://localhost:" + port + "/api/v1/products/all";
        response = restTemplate.getForEntity(url, ProductResponse[].class);
    }

    // LIAISON PHRASE 3
    @Alors("la liste doit contenir {int} produit")
    public void laListeDoitContenirProduit(int expectedSize) {
        assertThat(response.getBody()).isNotNull().hasSize(expectedSize);
    }

    // LIAISON PHRASE 4
    @Et("le nom du premier produit doit être {string}")
    public void leNomDuPremierProduitDoitEtre(String expectedName) {
        assertThat(response.getBody()[0].name()).isEqualTo(expectedName);
    }


    @Quand("le client demande la première page du catalogue avec {int} seul produit par page")
    public void leClientDemandeLaPremièrePageDuCatalogueAvecSeulProduitParPage(int size) {
        // page=0 (première page) et size passé en paramètre
        String url = "http://localhost:" + port + "/api/v1/products?page=0&size=" + size;
        paginationResponse = restTemplate.getForEntity(url, String.class);
    }

    @Alors("le catalogue doit indiquer qu'il y a {int} produits au total")
    public void leCatalogueDoitIndiquerQuIlYACatalogueProduitsAuTotal(int totalElements) {
        assertThat(paginationResponse.getBody()).contains("\"totalElements\":" + totalElements);
    }

    @Et("la page actuelle doit contenir exactement {int} produit")
    public void laPageActuelleDoitContenirExactementProduit(int sizeInPage) {
        // En Spring Data Page, la liste des éléments est encapsulée dans un tableau nommé "content"
        // On vérifie simplement que la taille reçue est la bonne
        assertThat(paginationResponse.getBody()).contains("\"size\":" + sizeInPage);
    }

    @Etque("le catalogue contient contient un produit {string} au prix de {bigdecimal}")
    public void leCatalogueContientContientUnProduitAuPrixDe(String name, BigDecimal price) {
        ProductEntity entity = new ProductEntity(name, "Deuxième composant", price, 10);
        productRepository.save(entity);
    }
}
