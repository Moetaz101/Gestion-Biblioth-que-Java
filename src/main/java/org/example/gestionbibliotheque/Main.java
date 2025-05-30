package org.example.gestionbibliotheque;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.gestionbibliotheque.dao.Database;

/**
 * Classe principale de l'application de gestion de bibliothèque
 * Cette classe initialise l'interface utilisateur et la base de données
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Initialiser la base de données
            System.out.println("Utilisation du fichier de base de données: " + new java.io.File("bibliotheque.db").getAbsolutePath());
            Database.initDatabase();

            // Charger le FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Scene scene = new Scene(loader.load());
            
            // Appliquer le CSS (bien que déjà chargé dans le FXML, cela assure la compatibilité)
            scene.getStylesheets().add(getClass().getResource("/css/light-blue-theme.css").toExternalForm());
            
            // Configurer la fenêtre principale
            primaryStage.setTitle("Gestion de Bibliothèque");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Erreur lors du démarrage de l'application: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}