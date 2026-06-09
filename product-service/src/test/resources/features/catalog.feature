# language: fr
Fonctionnalité: Gestion du catalogue de composants informatiques

  Scénario: Consultation de la liste des composants disponibles
    Étant donné que le catalogue contient un produit "NVIDIA RTX 5090" au prix de 2499,99
    Quand le client demande à voir tous les produits du catalogue
    Alors la liste doit contenir 1 produit
    Et le nom du premier produit doit être "NVIDIA RTX 5090"


  Scénario: Consultation du catalogue avec découpage par page
    Étant donné que le catalogue contient un produit "AMD Ryzen 9" au prix de 649,00
    Et que le catalogue contient contient un produit "NVIDIA RTX 5090" au prix de 2499,99
    Quand le client demande la première page du catalogue avec 1 seul produit par page
    Alors le catalogue doit indiquer qu'il y a 2 produits au total
    Et la page actuelle doit contenir exactement 1 produit
