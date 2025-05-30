package org.example.gestionbibliotheque.model;

import javafx.beans.property.*;
import java.time.LocalDate;

/**
 * Classe représentant un emprunt de livre dans la bibliothèque
 * Cette classe utilise les propriétés JavaFX pour faciliter l'affichage dans l'interface
 */
public class Emprunt {
    // Propriétés pour stocker les informations de l'emprunt
    private final IntegerProperty id;                // Identifiant unique de l'emprunt
    private final IntegerProperty livreId;            // Identifiant du livre emprunté
    private final IntegerProperty adherentId;          // Identifiant de l'adhérent qui a emprunté le livre
    private final ObjectProperty<LocalDate> dateEmprunt;  // Date d'emprunt du livre
    private final ObjectProperty<LocalDate> dateRetourPrevue; // Date de retour prévue du livre
    private final ObjectProperty<LocalDate> dateRetourEffective; // Date de retour effective du livre

    /**
     * Constructeur par défaut
     * Crée un emprunt vide avec des propriétés initialisées
     */
    public Emprunt() {
        this.id = new SimpleIntegerProperty();
        this.livreId = new SimpleIntegerProperty();
        this.adherentId = new SimpleIntegerProperty();
        this.dateEmprunt = new SimpleObjectProperty<>();
        this.dateRetourPrevue = new SimpleObjectProperty<>();
        this.dateRetourEffective = new SimpleObjectProperty<>();
    }

    /**
     * Constructeur avec paramètres
     * Crée un emprunt avec les informations fournies
     * @param livreId Identifiant du livre emprunté
     * @param adherentId Identifiant de l'adhérent qui emprunte
     * @param dateEmprunt Date d'emprunt du livre
     * @param dateRetourPrevue Date de retour prévue du livre
     */
    public Emprunt(int livreId, int adherentId, LocalDate dateEmprunt, LocalDate dateRetourPrevue) {
        this.id = new SimpleIntegerProperty();
        this.livreId = new SimpleIntegerProperty(livreId);
        this.adherentId = new SimpleIntegerProperty(adherentId);
        this.dateEmprunt = new SimpleObjectProperty<>(dateEmprunt);
        this.dateRetourPrevue = new SimpleObjectProperty<>(dateRetourPrevue);
        this.dateRetourEffective = new SimpleObjectProperty<>();
    }

    // Méthodes pour accéder aux propriétés (utilisées par JavaFX)
    public IntegerProperty idProperty() {
        return id;
    }

    public IntegerProperty livreIdProperty() {
        return livreId;
    }

    public IntegerProperty adherentIdProperty() {
        return adherentId;
    }

    public ObjectProperty<LocalDate> dateEmpruntProperty() {
        return dateEmprunt;
    }

    public ObjectProperty<LocalDate> dateRetourPrevueProperty() {
        return dateRetourPrevue;
    }

    public ObjectProperty<LocalDate> dateRetourEffectiveProperty() {
        return dateRetourEffective;
    }

    // Getters et setters classiques
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getLivreId() {
        return livreId.get();
    }

    public void setLivreId(int livreId) {
        this.livreId.set(livreId);
    }

    public int getAdherentId() {
        return adherentId.get();
    }

    public void setAdherentId(int adherentId) {
        this.adherentId.set(adherentId);
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt.get();
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt.set(dateEmprunt);
    }

    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue.get();
    }

    public void setDateRetourPrevue(LocalDate dateRetourPrevue) {
        this.dateRetourPrevue.set(dateRetourPrevue);
    }

    public LocalDate getDateRetourEffective() {
        return dateRetourEffective.get();
    }

    public void setDateRetourEffective(LocalDate dateRetourEffective) {
        this.dateRetourEffective.set(dateRetourEffective);
    }

    /**
     * Retourne une représentation textuelle de l'emprunt
     * @return Une chaîne de caractères contenant les informations de l'emprunt
     */
    @Override
    public String toString() {
        return "Emprunt{" +
                "id=" + id.get() +
                ", livreId=" + livreId.get() +
                ", adherentId=" + adherentId.get() +
                ", dateEmprunt=" + dateEmprunt.get() +
                ", dateRetourPrevue=" + dateRetourPrevue.get() +
                ", dateRetourEffective=" + dateRetourEffective.get() +
                '}';
    }
} 