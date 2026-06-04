package com.sohardware.productservice.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration// Dit à Cucumber qu'il doit démarrer le moteur complet de Spring Boot en tâche de fond
// ensuite Cucumber va analyser toutes les classes qui contiennent des annotations de phrases comme @Quand ou @Alors..
//puis prend le contrôle de ces classes les enregistre lui-même comme des Beans temporaires à l'intérieur du moteur Spring
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Démarre le microservice sur un port aléatoire pour les tests fonctionnels
public class CucumberSpringConfiguration {
    // Cette classe reste vide, elle sert uniquement de déclencheur de contexte pour Cucumber !
}
