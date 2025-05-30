# Gestion de Bibliothèque

Une application de gestion de bibliothèque complète développée en Java avec JavaFX.

## Fonctionnalités

### Gestion des Livres
- Ajouter, modifier et supprimer des livres
- Consulter l'inventaire complet des livres
- Rechercher des livres par titre ou auteur
- Suivi du nombre d'exemplaires disponibles

### Gestion des Adhérents
- Ajouter, modifier et supprimer des adhérents
- Consulter la liste complète des adhérents
- Rechercher des adhérents par nom, prénom ou email
- Validation des informations (email, téléphone, etc.)

### Gestion des Emprunts
- Créer de nouveaux emprunts
- Enregistrer les retours de livres
- Filtrer les emprunts (tous, en cours, retournés)
- Gestion automatique du stock d'exemplaires
- Date de retour prévue calculée automatiquement

## Architecture du Projet

### Structure MVC (Modèle-Vue-Contrôleur)

#### Modèles
- `Livre` : Représente un livre avec ses attributs (titre, auteur, nombre d'exemplaires)
- `Adherent` : Représente un adhérent avec ses informations personnelles
- `Emprunt` : Représente un emprunt avec dates et références au livre et à l'adhérent

#### DAO (Data Access Objects)
- `LivreDAO` : Gère les opérations de base de données pour les livres
- `AdherentDAO` : Gère les opérations de base de données pour les adhérents
- `EmpruntDAO` : Gère les opérations de base de données pour les emprunts

#### Contrôleur
- `MainController` : Contrôleur principal qui gère toutes les interactions utilisateur

#### Vue
- Fichiers FXML dans `src/main/resources/fxml/`
- Feuilles de style CSS dans `src/main/resources/css/`

### Base de Données
- SQLite pour le stockage des données
- Transactions gérées pour assurer l'intégrité des données
- Validation des entrées avant insertion/mise à jour

## Installation et Exécution

### Prérequis
- JDK 11 ou supérieur
- Maven pour la gestion des dépendances

### Étapes d'installation
1. Cloner le dépôt :
   ```
   git clone [URL_DU_REPO]
   ```

2. Naviguer vers le dossier du projet :
   ```
   cd GestionBibliotheque
   ```

3. Construire le projet avec Maven :
   ```
   mvn clean package
   ```

4. Exécuter l'application :
   ```
   java -jar target/GestionBibliotheque-1.0-SNAPSHOT.jar
   ```

## Utilisation

### Gestion des Livres
- Ajout de livre : Remplir les champs Titre, Auteur et Nombre d'exemplaires puis cliquer sur "Ajouter Livre"
- Recherche : Saisir un texte dans le champ de recherche et cliquer sur "Rechercher"
- Modification : Sélectionner un livre dans la liste et cliquer sur "Modifier"
- Suppression : Sélectionner un livre dans la liste et cliquer sur "Supprimer"

### Gestion des Adhérents
- Ajout d'adhérent : Remplir les champs Nom, Prénom, Email et Téléphone puis cliquer sur "Ajouter Adhérent"
- Recherche : Saisir un texte dans le champ de recherche et cliquer sur "Rechercher"
- Modification : Sélectionner un adhérent dans la liste et cliquer sur "Modifier"
- Suppression : Sélectionner un adhérent dans la liste et cliquer sur "Supprimer"

### Gestion des Emprunts
- Création d'emprunt : Sélectionner un livre et un adhérent dans les listes déroulantes puis cliquer sur "Créer Emprunt"
- Filtrage : Sélectionner le type d'emprunts à afficher et cliquer sur "Filtrer"
- Retour de livre : Sélectionner un emprunt dans la liste et cliquer sur "Retourner le livre"
- Suppression : Sélectionner un emprunt dans la liste et cliquer sur "Supprimer"

## Validation des Données

L'application inclut une validation complète des données :
- Noms et prénoms : Doivent contenir uniquement des lettres et des espaces
- Email : Doit suivre un format valide
- Téléphone : Doit contenir exactement 8 chiffres
- Nombre d'exemplaires : Doit être un nombre entier positif

## Gestion des Erreurs

- Transactions pour assurer l'intégrité des données
- Messages d'erreur explicites pour guider l'utilisateur
- Validation des données avant insertion en base
- Gestion des exceptions avec affichage des messages appropriés

## Améliorations Futures Possibles

- Authentification des utilisateurs avec différents niveaux d'accès
- Rapports statistiques sur les emprunts et retours
- Système de réservation de livres
- Notifications pour les retards
- Export des données en différents formats (PDF, Excel)
- Interface multilingue
