package org.example.gestionbibliotheque.model;

import javafx.beans.property.*;

/**
 * Classe représentant un livre dans la bibliothèque
 * Cette classe utilise les propriétés JavaFX pour faciliter l'affichage dans l'interface
 */
public class Livre {
    // Propriétés pour stocker les informations du livre
    private final IntegerProperty id;              // Identifiant unique du livre
    private final StringProperty titre;           // Titre du livre
    private final StringProperty auteur;          // Auteur du livre
    private final IntegerProperty nbExemplaires;  // Nombre d'exemplaires disponibles

    /**
     * Constructeur par défaut
     * Crée un livre vide avec des propriétés initialisées
     */
    public Livre() {
        this.id = new SimpleIntegerProperty();
        this.titre = new SimpleStringProperty();
        this.auteur = new SimpleStringProperty();
        this.nbExemplaires = new SimpleIntegerProperty();
    }

    /**
     * Constructeur avec paramètres
     * Crée un livre avec les informations fournies
     * @param titre Le titre du livre
     * @param auteur L'auteur du livre
     * @param nbExemplaires Le nombre d'exemplaires
     */
    public Livre(String titre, String auteur, int nbExemplaires) {
        this.id = new SimpleIntegerProperty();
        this.titre = new SimpleStringProperty(titre);
        this.auteur = new SimpleStringProperty(auteur);
        this.nbExemplaires = new SimpleIntegerProperty(nbExemplaires);
    }

    // Méthodes pour accéder aux propriétés (utilisées par JavaFX)
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty titreProperty() {
        return titre;
    }

    public StringProperty auteurProperty() {
        return auteur;
    }

    public IntegerProperty nbExemplairesProperty() {
        return nbExemplaires;
    }

    // Getters et setters classiques
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getTitre() {
        return titre.get();
    }

    public void setTitre(String titre) {
        this.titre.set(titre);
    }

    public String getAuteur() {
        return auteur.get();
    }

    public void setAuteur(String auteur) {
        this.auteur.set(auteur);
    }

    public int getNbExemplaires() {
        return nbExemplaires.get();
    }

    public void setNbExemplaires(int nbExemplaires) {
        this.nbExemplaires.set(nbExemplaires);
    }

    /**
     * Retourne une représentation textuelle du livre
     * @return Une chaîne de caractères contenant les informations du livre
     */
    @Override
    public String toString() {
        return "Livre{" +
                "id=" + id.get() +
                ", titre='" + titre.get() + '\'' +
                ", auteur='" + auteur.get() + '\'' +
                ", nbExemplaires=" + nbExemplaires.get() +
                '}';
    }
} 