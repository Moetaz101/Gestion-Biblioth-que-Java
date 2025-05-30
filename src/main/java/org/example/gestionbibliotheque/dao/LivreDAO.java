package org.example.gestionbibliotheque.dao;

import org.example.gestionbibliotheque.model.Livre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe d'accès aux données pour les livres
 * Cette classe gère toutes les opérations de base de données liées aux livres
 */
public class LivreDAO {

    /**
     * Ajoute un nouveau livre dans la base de données
     * @param livre Le livre à ajouter
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données
     */
    public void ajouterLivre(Livre livre) throws SQLException, IllegalArgumentException {
        if (livre == null) {
            throw new IllegalArgumentException("Le livre ne peut pas être null");
        }
        
        // Validation des données
        if (!org.example.gestionbibliotheque.util.ValidationUtil.estTitreValide(livre.getTitre())) {
            throw new IllegalArgumentException("Le titre du livre n'est pas valide. Il ne doit pas contenir de symboles spéciaux.");
        }
        
        if (!org.example.gestionbibliotheque.util.ValidationUtil.estNomValide(livre.getAuteur())) {
            throw new IllegalArgumentException("Le nom de l'auteur n'est pas valide. Il doit contenir uniquement des lettres et des espaces.");
        }
        
        if (!org.example.gestionbibliotheque.util.ValidationUtil.estNombreExemplairesValide(livre.getNbExemplaires())) {
            throw new IllegalArgumentException("Le nombre d'exemplaires doit être un nombre positif.");
        }
        
        String sql = "INSERT INTO Livres (titre, auteur, nb_exemplaires) VALUES (?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            try {
                stmt.setString(1, livre.getTitre());
                stmt.setString(2, livre.getAuteur());
                stmt.setInt(3, livre.getNbExemplaires());
                stmt.executeUpdate();
                
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        livre.setId(generatedKeys.getInt(1));
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    /**
     * Récupère tous les livres de la base de données
     * @return Une liste de tous les livres
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données
     */
    public List<Livre> getLivres() throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM Livres";
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Livre livre = new Livre();
                livre.setId(rs.getInt("id"));
                
                String titre = rs.getString("titre");
                if (titre != null) {
                    livre.setTitre(titre);
                }
                
                String auteur = rs.getString("auteur");
                if (auteur != null) {
                    livre.setAuteur(auteur);
                }
                
                livre.setNbExemplaires(rs.getInt("nb_exemplaires"));
                livres.add(livre);
            }
        }
        return livres;
    }

    // Read - Get by ID
    public Livre getLivreById(int id) throws SQLException {
        String sql = "SELECT * FROM Livres WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Livre livre = new Livre();
                    livre.setId(rs.getInt("id"));
                    livre.setTitre(rs.getString("titre"));
                    livre.setAuteur(rs.getString("auteur"));
                    livre.setNbExemplaires(rs.getInt("nb_exemplaires"));
                    return livre;
                }
            }
        }
        return null;
    }

    // Read - Search by title
    public List<Livre> rechercherParTitre(String titre) throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM Livres WHERE titre LIKE ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + titre + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Livre livre = new Livre();
                    livre.setId(rs.getInt("id"));
                    livre.setTitre(rs.getString("titre"));
                    livre.setAuteur(rs.getString("auteur"));
                    livre.setNbExemplaires(rs.getInt("nb_exemplaires"));
                    livres.add(livre);
                }
            }
        }
        return livres;
    }

    // Read - Search by author
    public List<Livre> rechercherParAuteur(String auteur) throws SQLException {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM Livres WHERE auteur LIKE ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + auteur + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Livre livre = new Livre();
                    livre.setId(rs.getInt("id"));
                    livre.setTitre(rs.getString("titre"));
                    livre.setAuteur(rs.getString("auteur"));
                    livre.setNbExemplaires(rs.getInt("nb_exemplaires"));
                    livres.add(livre);
                }
            }
        }
        return livres;
    }

    // Update
    /**
     * Modifie un livre existant dans la base de données
     * @param livre Le livre à modifier avec les nouvelles valeurs
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données
     * @throws IllegalArgumentException Si le livre est null
     */
    public void modifierLivre(Livre livre) throws SQLException, IllegalArgumentException {
        if (livre == null) {
            throw new IllegalArgumentException("Le livre ne peut pas être null");
        }
        
        // Validation des données
        if (!org.example.gestionbibliotheque.util.ValidationUtil.estTitreValide(livre.getTitre())) {
            throw new IllegalArgumentException("Le titre du livre n'est pas valide. Il ne doit pas contenir de symboles spéciaux.");
        }
        
        if (!org.example.gestionbibliotheque.util.ValidationUtil.estNomValide(livre.getAuteur())) {
            throw new IllegalArgumentException("Le nom de l'auteur n'est pas valide. Il doit contenir uniquement des lettres et des espaces.");
        }
        
        if (!org.example.gestionbibliotheque.util.ValidationUtil.estNombreExemplairesValide(livre.getNbExemplaires())) {
            throw new IllegalArgumentException("Le nombre d'exemplaires doit être un nombre positif.");
        }
        
        String sql = "UPDATE Livres SET titre = ?, auteur = ?, nb_exemplaires = ? WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            try {
                stmt.setString(1, livre.getTitre());
                stmt.setString(2, livre.getAuteur());
                stmt.setInt(3, livre.getNbExemplaires());
                stmt.setInt(4, livre.getId());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Livre non trouvé avec l'ID: " + livre.getId());
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    // Delete
    public void supprimerLivre(int id) throws SQLException {
        String sql = "DELETE FROM Livres WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            try {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Livre non trouvé avec l'ID: " + id);
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }
}