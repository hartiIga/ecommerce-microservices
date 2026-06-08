package com.sohardware.productservice.cucumber;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite // Dit à JUnit 5 d'exécuter une suite de tests complets
@IncludeEngines("cucumber") // Force JUnit à utiliser le moteur Cucumber pour cette suite
@SelectClasspathResource("features") // Indique où chercher nos fichiers. feature en français
@ConfigurationParameter(
        key = Constants.GLUE_PROPERTY_NAME,
        value = "com.sohardware.productservice.cucumber" //On dit explicitement à JUnit où sont rangés nos fichiers Java de liaison (Glue)
)
@ConfigurationParameter(
        key = Constants.PLUGIN_PROPERTY_NAME,
        value = "pretty, html:target/cucumber-reports/cucumber.html" // Génère un superbe rapport visuel en HTML dans le dossier target
)
public class RunCucumberTest {
    // Cette classe reste vide. C'est uniquement un panneau de signalisation pour JUnit !
}
