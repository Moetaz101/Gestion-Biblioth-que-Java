package org.example.gestionbibliotheque.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Classe utilitaire pour gérer les connexions à la base de données
 * Cette classe fournit des méthodes pour établir et fermer des connexions
 */
public class Database {

    private static final String URL = "jdbc:sqlite:bibliotheque.db"; // Chemin vers la base de données SQLite
    
    /**
     * Obtient une nouvelle connexion à la base de données
     * @return Une connexion à la base de données
     * @throws SQLException Si une erreur survient lors de la connexion
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Méthode pour initialiser la base de données
    public static void initDatabase() {
        String dropLivresSQL = "DROP TABLE IF EXISTS Livres;";
        String createLivresSQL = """
            CREATE TABLE IF NOT EXISTS Livres (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titre TEXT NOT NULL,
                auteur TEXT NOT NULL,
                nb_exemplaires INTEGER NOT NULL
            );
        """;

        // Suppression de la table Adherents si elle existe pour la recréer avec la structure correcte
        String dropAdherentsSQL = "DROP TABLE IF EXISTS Adherents;";
        
        String createAdherentsSQL = """
            CREATE TABLE IF NOT EXISTS Adherents (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT NOT NULL,
                prenom TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                telephone TEXT NOT NULL
            );
        """;

        String dropEmpruntsSQL = "DROP TABLE IF EXISTS Emprunts;";
        String createEmpruntsSQL = """
            CREATE TABLE IF NOT EXISTS Emprunts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                date_emprunt DATE NOT NULL,
                date_retour_prevue DATE NOT NULL,
                date_retour_effective DATE,
                livre_id INTEGER NOT NULL,
                adherent_id INTEGER NOT NULL,
                FOREIGN KEY(livre_id) REFERENCES Livres(id),
                FOREIGN KEY(adherent_id) REFERENCES Adherents(id)
            );
        """;
           
        

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {

            // Exécuter chaque instruction SQL séparément
            stmt.execute(dropLivresSQL);
            stmt.execute(createLivresSQL);

            stmt.execute(dropAdherentsSQL);
            stmt.execute(createAdherentsSQL);

            stmt.execute(dropEmpruntsSQL);
            stmt.execute(createEmpruntsSQL);

            System.out.println("Tables créées avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création des tables : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Ferme une connexion à la base de données de manière sécurisée
     * @param connection La connexion à fermer
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
}

