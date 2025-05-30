package org.example.gestionbibliotheque.model;

import javafx.beans.property.*;

/**
 * Classe représentant un adhérent de la bibliothèque
 * Cette classe utilise les propriétés JavaFX pour faciliter l'affichage dans l'interface
 */
public class Adherent {
    // Propriétés pour stocker les informations de l'adhérent
    private final IntegerProperty id;        // Identifiant unique de l'adhérent
    private final StringProperty nom;        // Nom de l'adhérent
    private final StringProperty prenom;     // Prénom de l'adhérent
    private final StringProperty email;      // Adresse email de l'adhérent
    private final StringProperty telephone;  // Numéro de téléphone de l'adhérent

    /**
     * Constructeur par défaut
     * Crée un adhérent vide avec des propriétés initialisées
     */
    public Adherent() {
        this.id = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty();
        this.prenom = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.telephone = new SimpleStringProperty();
    }

    /**
     * Constructeur avec paramètres
     * Crée un adhérent avec les informations fournies
     * @param nom Le nom de l'adhérent
     * @param prenom Le prénom de l'adhérent
     * @param email L'adresse email de l'adhérent
     * @param telephone Le numéro de téléphone de l'adhérent
     */
    public Adherent(String nom, String prenom, String email, String telephone) {
        this.id = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.email = new SimpleStringProperty(email);
        this.telephone = new SimpleStringProperty(telephone);
    }

    // Méthodes pour accéder aux propriétés (utilisées par JavaFX)
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public StringProperty prenomProperty() {
        return prenom;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty telephoneProperty() {
        return telephone;
    }

    // Getters et setters classiques
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getPrenom() {
        return prenom.get();
    }

    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getTelephone() {
        return telephone.get();
    }

    public void setTelephone(String telephone) {
        this.telephone.set(telephone);
    }

    /**
     * Retourne une représentation textuelle de l'adhérent
     * @return Une chaîne de caractères contenant les informations de l'adhérent
     */
    @Override
    public String toString() {
        return "Adherent{" +
                "id=" + id.get() +
                ", nom='" + nom.get() + '\'' +
                ", prenom='" + prenom.get() + '\'' +
                ", email='" + email.get() + '\'' +
                ", telephone='" + telephone.get() + '\'' +
                '}';
    }
} 