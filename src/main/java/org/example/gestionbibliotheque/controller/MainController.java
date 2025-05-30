package org.example.gestionbibliotheque.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.gestionbibliotheque.dao.LivreDAO;
import org.example.gestionbibliotheque.dao.AdherentDAO;
import org.example.gestionbibliotheque.dao.EmpruntDAO;
import org.example.gestionbibliotheque.model.Livre;
import org.example.gestionbibliotheque.model.Adherent;
import org.example.gestionbibliotheque.model.Emprunt;
import java.sql.SQLException;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Contrôleur principal de l'application
 * Cette classe gère l'interface utilisateur et les interactions avec la base de données
 */
public class MainController {

    // Champs pour les livres
    @FXML private TextField titreField;
    @FXML private TextField auteurField;
    @FXML private TextField nbExemplairesField;
    @FXML private TextField rechercheLivreField;
    @FXML private TableView<Livre> livresTable;
    @FXML private TableColumn<Livre, Integer> idColumn;
    @FXML private TableColumn<Livre, String> titreColumn;
    @FXML private TableColumn<Livre, String> auteurColumn;
    @FXML private TableColumn<Livre, Integer> nbExemplairesColumn;

    // Champs pour les adhérents
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField telephoneField;
    @FXML private TextField rechercheAdherentField;
    @FXML private TableView<Adherent> adherentsTable;
    @FXML private TableColumn<Adherent, Integer> adherentIdColumn;
    @FXML private TableColumn<Adherent, String> nomColumn;
    @FXML private TableColumn<Adherent, String> prenomColumn;
    @FXML private TableColumn<Adherent, String> emailColumn;
    @FXML private TableColumn<Adherent, String> telephoneColumn;

    // Champs pour les emprunts
    @FXML private ComboBox<Livre> livreComboBox;
    @FXML private ComboBox<Adherent> adherentComboBox;
    @FXML private ComboBox<String> filtreEmpruntComboBox;
    @FXML private TableView<Emprunt> empruntsTable;
    @FXML private TableColumn<Emprunt, Integer> empruntIdColumn;
    @FXML private TableColumn<Emprunt, LocalDate> dateEmpruntColumn;
    @FXML private TableColumn<Emprunt, LocalDate> dateRetourPrevueColumn;
    @FXML private TableColumn<Emprunt, LocalDate> dateRetourEffectiveColumn;
    @FXML private TableColumn<Emprunt, String> livreColumn;
    @FXML private TableColumn<Emprunt, String> adherentColumn;

    // Objets pour gérer les données
    private LivreDAO livreDAO;
    private AdherentDAO adherentDAO;
    private EmpruntDAO empruntDAO;
    private ObservableList<Livre> livres;
    private ObservableList<Adherent> adherents;
    private ObservableList<Emprunt> emprunts;

    private FilteredList<Livre> filteredLivres;
    private FilteredList<Adherent> filteredAdherents;
    private FilteredList<Emprunt> filteredEmprunts;

    /**
     * Méthode appelée automatiquement au démarrage de l'interface
     * Elle initialise les composants et charge les données
     */
    @FXML
    public void initialize() {
        // Initialiser les DAOs
        livreDAO = new LivreDAO();
        adherentDAO = new AdherentDAO();
        empruntDAO = new EmpruntDAO();
        
        // Initialiser les listes observables
        livres = FXCollections.observableArrayList();
        adherents = FXCollections.observableArrayList();
        emprunts = FXCollections.observableArrayList();
        
        // Configurer les colonnes des livres
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        titreColumn.setCellValueFactory(cellData -> cellData.getValue().titreProperty());
        auteurColumn.setCellValueFactory(cellData -> cellData.getValue().auteurProperty());
        nbExemplairesColumn.setCellValueFactory(cellData -> cellData.getValue().nbExemplairesProperty().asObject());
        
        // Configurer les colonnes des adhérents
        adherentIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        prenomColumn.setCellValueFactory(cellData -> cellData.getValue().prenomProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        telephoneColumn.setCellValueFactory(cellData -> cellData.getValue().telephoneProperty());
        
        // Configurer les colonnes des emprunts
        empruntIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        dateEmpruntColumn.setCellValueFactory(cellData -> cellData.getValue().dateEmpruntProperty());
        dateRetourPrevueColumn.setCellValueFactory(cellData -> cellData.getValue().dateRetourPrevueProperty());
        dateRetourEffectiveColumn.setCellValueFactory(cellData -> cellData.getValue().dateRetourEffectiveProperty());
        livreColumn.setCellValueFactory(cellData -> {
            try {
                Livre livre = livreDAO.getLivreById(cellData.getValue().getLivreId());
                return new SimpleStringProperty(livre != null ? livre.getTitre() : "");
            } catch (SQLException e) {
                return new SimpleStringProperty("");
            }
        });
        adherentColumn.setCellValueFactory(cellData -> {
            try {
                Adherent adherent = adherentDAO.getAdherentById(cellData.getValue().getAdherentId());
                return new SimpleStringProperty(adherent != null ? adherent.getNom() : "");
            } catch (SQLException e) {
                return new SimpleStringProperty("");
            }
        });
        
        // Associer les listes aux tables
        livresTable.setItems(livres);
        adherentsTable.setItems(adherents);
        empruntsTable.setItems(emprunts);
        
        // Configurer les ComboBox
        livreComboBox.setItems(livres);
        adherentComboBox.setItems(adherents);
        
        // Charger les données
        chargerLivres();
        chargerAdherents();
        chargerEmprunts();

        setupSearchFilters();
        setupComboBoxes();
    }

    private void setupSearchFilters() {
        filteredLivres = new FilteredList<>(livres);
        filteredAdherents = new FilteredList<>(adherents);
        filteredEmprunts = new FilteredList<>(emprunts);
        
        livresTable.setItems(filteredLivres);
        adherentsTable.setItems(filteredAdherents);
        empruntsTable.setItems(filteredEmprunts);
    }

    private void setupComboBoxes() {
        filtreEmpruntComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Méthode appelée quand on clique sur le bouton "Ajouter Livre"
     */
    @FXML
    private void ajouterLivre() {
        try {
            String titre = titreField.getText();
            String auteur = auteurField.getText();
            int nbExemplaires = Integer.parseInt(nbExemplairesField.getText());

            if (titre.isEmpty() || auteur.isEmpty()) {
                afficherMessage("Erreur", "Veuillez remplir tous les champs");
                return;
            }

            Livre livre = new Livre(titre, auteur, nbExemplaires);
            livreDAO.ajouterLivre(livre);
            
            titreField.clear();
            auteurField.clear();
            nbExemplairesField.clear();
            
            chargerLivres();
            afficherMessage("Succès", "Livre ajouté avec succès");
            
        } catch (NumberFormatException e) {
            afficherMessage("Erreur", "Le nombre d'exemplaires doit être un nombre entier");
        } catch (IllegalArgumentException e) {
            afficherMessage("Erreur de validation", e.getMessage());
        } catch (SQLException e) {
            afficherMessage("Erreur", "Erreur lors de l'ajout du livre: " + e.getMessage());
        }
    }

    /**
     * Méthode appelée quand on clique sur le bouton "Ajouter Adhérent"
     */
    @FXML
    private void ajouterAdherent() {
        try {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String email = emailField.getText();
            String telephone = telephoneField.getText();

            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || telephone.isEmpty()) {
                afficherMessage("Erreur", "Veuillez remplir tous les champs");
                return;
            }

            Adherent adherent = new Adherent(nom, prenom, email, telephone);
            adherentDAO.ajouterAdherent(adherent);
            
            nomField.clear();
            prenomField.clear();
            emailField.clear();
            telephoneField.clear();
            
            chargerAdherents();
            afficherMessage("Succès", "Adhérent ajouté avec succès");
            
        } catch (IllegalArgumentException e) {
            afficherMessage("Erreur de validation", e.getMessage());
        } catch (SQLException e) {
            afficherMessage("Erreur", "Erreur lors de l'ajout de l'adhérent: " + e.getMessage());
        }
    }

    /**
     * Méthode appelée quand on clique sur le bouton "Créer Emprunt"
     */
    @FXML
    private void creerEmprunt() {
        Livre selectedLivre = livreComboBox.getValue();
        Adherent selectedAdherent = adherentComboBox.getValue();
        
        if (selectedLivre == null || selectedAdherent == null) {
            afficherMessage("Erreur", "Veuillez sélectionner un livre et un adhérent");
            return;
        }

        if (selectedLivre.getNbExemplaires() <= 0) {
            afficherMessage("Erreur", "Il n'y a plus d'exemplaires disponibles");
            return;
        }

        try {
            Emprunt emprunt = new Emprunt();
            emprunt.setLivreId(selectedLivre.getId());
            emprunt.setAdherentId(selectedAdherent.getId());
            emprunt.setDateEmprunt(LocalDate.now());
            emprunt.setDateRetourPrevue(LocalDate.now().plusDays(14)); // 2 semaines de prêt par défaut
            
            empruntDAO.ajouterEmprunt(emprunt);
            
            selectedLivre.setNbExemplaires(selectedLivre.getNbExemplaires() - 1);
            livreDAO.modifierLivre(selectedLivre);
            
            emprunts.add(emprunt);
            livresTable.refresh();
            afficherMessage("Succès", "Emprunt créé avec succès");
        } catch (Exception e) {
            afficherMessage("Erreur", "Erreur lors de la création de l'emprunt: " + e.getMessage());
        }
    }

    /**
     * Charge la liste des livres depuis la base de données
     */
    private void chargerLivres() {
        try {
            livres.clear();
            livres.addAll(livreDAO.getLivres());
        } catch (SQLException e) {
            afficherMessage("Erreur", "Erreur lors du chargement des livres: " + e.getMessage());
        }
    }

    /**
     * Charge la liste des adhérents depuis la base de données
     */
    private void chargerAdherents() {
        try {
            adherents.clear();
            adherents.addAll(adherentDAO.getAdherents());
        } catch (SQLException e) {
            afficherMessage("Erreur", "Erreur lors du chargement des adhérents: " + e.getMessage());
        }
    }

    /**
     * Charge la liste des emprunts depuis la base de données
     */
    private void chargerEmprunts() {
        try {
            emprunts.clear();
            emprunts.addAll(empruntDAO.getEmprunts());
        } catch (SQLException e) {
            afficherMessage("Erreur", "Erreur lors du chargement des emprunts: " + e.getMessage());
        }
    }

    /**
     * Affiche une boîte de dialogue d'information avec un message
     * @param titre Le titre de la boîte de dialogue
     * @param message Le message à afficher
     */
    private void afficherMessage(String titre, String message) {
        showAlert(titre, message, Alert.AlertType.INFORMATION);
    }

    @FXML
    private void modifierLivre() {
        Livre selectedLivre = livresTable.getSelectionModel().getSelectedItem();
        if (selectedLivre == null) {
            afficherMessage("Erreur", "Veuillez sélectionner un livre à modifier");
            return;
        }

        Dialog<Livre> dialog = new Dialog<>();
        dialog.setTitle("Modifier le livre");
        dialog.setHeaderText("Modifier les informations du livre");

        TextField titreField = new TextField(selectedLivre.getTitre());
        TextField auteurField = new TextField(selectedLivre.getAuteur());
        TextField nbExemplairesField = new TextField(String.valueOf(selectedLivre.getNbExemplaires()));

        dialog.getDialogPane().setContent(new VBox(10,
            new Label("Titre:"), titreField,
            new Label("Auteur:"), auteurField,
            new Label("Nombre d'exemplaires:"), nbExemplairesField
        ));

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                selectedLivre.setTitre(titreField.getText());
                selectedLivre.setAuteur(auteurField.getText());
                selectedLivre.setNbExemplaires(Integer.parseInt(nbExemplairesField.getText()));
                return selectedLivre;
            }
            return null;
        });

        Optional<Livre> result = dialog.showAndWait();
        result.ifPresent(livre -> {
            try {
                livreDAO.modifierLivre(livre);
                livresTable.refresh();
                afficherMessage("Succès", "Livre modifié avec succès");
            } catch (IllegalArgumentException e) {
                afficherMessage("Erreur de validation", e.getMessage());
            } catch (Exception e) {
                afficherMessage("Erreur", "Erreur lors de la modification du livre: " + e.getMessage());
            }
        });
    }

    @FXML
    private void supprimerLivre() {
        Livre selectedLivre = livresTable.getSelectionModel().getSelectedItem();
        if (selectedLivre == null) {
            afficherMessage("Erreur", "Veuillez sélectionner un livre à supprimer");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le livre");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce livre ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                livreDAO.supprimerLivre(selectedLivre.getId());
                livres.remove(selectedLivre);
                afficherMessage("Succès", "Livre supprimé avec succès");
            } catch (Exception e) {
                afficherMessage("Erreur", "Erreur lors de la suppression du livre: " + e.getMessage());
            }
        }
    }

    @FXML
    private void rechercherLivre() {
        String searchText = rechercheLivreField.getText().toLowerCase();
        filteredLivres.setPredicate(livre ->
            livre.getTitre().toLowerCase().contains(searchText) ||
            livre.getAuteur().toLowerCase().contains(searchText)
        );
    }

    @FXML
    private void modifierAdherent() {
        Adherent selectedAdherent = adherentsTable.getSelectionModel().getSelectedItem();
        if (selectedAdherent == null) {
            afficherMessage("Erreur", "Veuillez sélectionner un adhérent à modifier");
            return;
        }

        Dialog<Adherent> dialog = new Dialog<>();
        dialog.setTitle("Modifier l'adhérent");
        dialog.setHeaderText("Modifier les informations de l'adhérent");

        TextField nomField = new TextField(selectedAdherent.getNom());
        TextField prenomField = new TextField(selectedAdherent.getPrenom());
        TextField emailField = new TextField(selectedAdherent.getEmail());
        TextField telephoneField = new TextField(selectedAdherent.getTelephone());

        dialog.getDialogPane().setContent(new VBox(10,
            new Label("Nom:"), nomField,
            new Label("Prénom:"), prenomField,
            new Label("Email:"), emailField,
            new Label("Téléphone:"), telephoneField
        ));

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                selectedAdherent.setNom(nomField.getText());
                selectedAdherent.setPrenom(prenomField.getText());
                selectedAdherent.setEmail(emailField.getText());
                selectedAdherent.setTelephone(telephoneField.getText());
                return selectedAdherent;
            }
            return null;
        });

        Optional<Adherent> result = dialog.showAndWait();
        result.ifPresent(adherent -> {
            try {
                adherentDAO.modifierAdherent(adherent);
                adherentsTable.refresh();
                afficherMessage("Succès", "Adhérent modifié avec succès");
            } catch (IllegalArgumentException e) {
                afficherMessage("Erreur de validation", e.getMessage());
            } catch (Exception e) {
                afficherMessage("Erreur", "Erreur lors de la modification de l'adhérent: " + e.getMessage());
            }
        });
    }

    @FXML
    private void supprimerAdherent() {
        Adherent selectedAdherent = adherentsTable.getSelectionModel().getSelectedItem();
        if (selectedAdherent == null) {
            afficherMessage("Erreur", "Veuillez sélectionner un adhérent à supprimer");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'adhérent");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet adhérent ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                adherentDAO.supprimerAdherent(selectedAdherent.getId());
                adherents.remove(selectedAdherent);
                afficherMessage("Succès", "Adhérent supprimé avec succès");
            } catch (Exception e) {
                afficherMessage("Erreur", "Erreur lors de la suppression de l'adhérent: " + e.getMessage());
            }
        }
    }

    @FXML
    private void rechercherAdherent() {
        String searchText = rechercheAdherentField.getText().toLowerCase();
        filteredAdherents.setPredicate(adherent ->
            adherent.getNom().toLowerCase().contains(searchText) ||
            adherent.getPrenom().toLowerCase().contains(searchText) ||
            adherent.getEmail().toLowerCase().contains(searchText)
        );
    }

    @FXML
    private void retournerLivre() {
        Emprunt selectedEmprunt = empruntsTable.getSelectionModel().getSelectedItem();
        if (selectedEmprunt == null) {
            afficherMessage("Erreur", "Veuillez sélectionner un emprunt");
            return;
        }

        if (selectedEmprunt.getDateRetourEffective() != null) {
            afficherMessage("Erreur", "Ce livre a déjà été retourné");
            return;
        }

        try {
            selectedEmprunt.setDateRetourEffective(LocalDate.now());
            empruntDAO.modifierEmprunt(selectedEmprunt);
            
            Livre livre = livreDAO.getLivreById(selectedEmprunt.getLivreId());
            livre.setNbExemplaires(livre.getNbExemplaires() + 1);
            livreDAO.modifierLivre(livre);
            
            empruntsTable.refresh();
            livresTable.refresh();
            afficherMessage("Succès", "Livre retourné avec succès");
        } catch (IllegalArgumentException e) {
            afficherMessage("Erreur de validation", e.getMessage());
        } catch (Exception e) {
            afficherMessage("Erreur", "Erreur lors du retour du livre: " + e.getMessage());
        }
    }

    @FXML
    private void supprimerEmprunt() {
        Emprunt selectedEmprunt = empruntsTable.getSelectionModel().getSelectedItem();
        if (selectedEmprunt == null) {
            afficherMessage("Erreur", "Veuillez sélectionner un emprunt à supprimer");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'emprunt");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet emprunt ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                empruntDAO.supprimerEmprunt(selectedEmprunt.getId());
                emprunts.remove(selectedEmprunt);
                afficherMessage("Succès", "Emprunt supprimé avec succès");
            } catch (Exception e) {
                afficherMessage("Erreur", "Erreur lors de la suppression de l'emprunt: " + e.getMessage());
            }
        }
    }

    @FXML
    private void filtrerEmprunts() {
        String filter = filtreEmpruntComboBox.getValue();
        if (filter == null) return;

        filteredEmprunts.setPredicate(emprunt -> {
            switch (filter) {
                case "En cours":
                    return emprunt.getDateRetourEffective() == null;
                case "Retournés":
                    return emprunt.getDateRetourEffective() != null;
                default:
                    return true;
            }
        });
    }

    /**
     * Affiche une boîte de dialogue avec un message
     * @param titre Le titre de la boîte de dialogue
     * @param message Le message à afficher
     * @param type Le type d'alerte (information, erreur, etc.)
     */
    private void showAlert(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}