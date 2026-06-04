# language: fr
Fonctionnalité: Gestion du catalogue de composants informatiques

  Scénario: Consultation de la liste des composants disponibles
    Étant donné que le catalogue contient un produit "NVIDIA RTX 5090" au prix de 2499,99
    Quand le client demande à voir tous les produits du catalogue
    Alors la liste doit contenir 1 produit
    Et le nom du premier produit doit être "NVIDIA RTX 5090"