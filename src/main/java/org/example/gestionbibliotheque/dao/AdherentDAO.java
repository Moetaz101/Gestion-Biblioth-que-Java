package org.example.gestionbibliotheque.dao;

import org.example.gestionbibliotheque.model.Adherent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe d'accès aux données pour les adhérents
 * Cette classe gère toutes les opérations de base de données liées aux adhérents
 */
public class AdherentDAO {

    /**
     * Ajoute un nouvel adhérent dans la base de données
     * @param adherent L'adhérent à ajouter
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données
     * @throws IllegalArgumentException Si l'adhérent est null
     */
    public void ajouterAdherent(Adherent adherent) throws SQLException, IllegalArgumentException {
        if (adherent == null) {
            throw new IllegalArgumentException("L'adhérent ne peut pas être null");
        }
        
        // Validation des données
        if (!org.example.gestionbibliotheque.util.ValidationUtil.estNomValide(adherent.getNom())) {
            throw new IllegalArgumentException("Le nom de l'adhérent n'est pas valide. Il doit contenir uniquement des lettres et des espaces.");
        }
        
        if (!org.example.gestionbibliotheque.util.ValidationUtil.estNomValide(adherent.getPrenom())) {
            throw new IllegalArgumentException("Le prénom de l'adhérent n'est pas valide. Il doit contenir uniquement des lettres et des espaces.");
        }
        
        if (!org.example.gestionbibliotheque.util.ValidationUtil.estEmailValide(adherent.getEmail())) {
            throw new IllegalArgumentException("L'email n'est pas valide. Veuillez saisir un email au format correct.");
        }
        
        if (!org.example.gestionbibliotheque.util.ValidationUtil.estTelephoneValide(adherent.getTelephone())) {
            throw new IllegalArgumentException("Le numéro de téléphone n'est pas valide. Il doit contenir exactement 8 chiffres.");
        }
        
        String sql = "INSERT INTO Adherents (nom, prenom, email, telephone) VALUES (?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            try {
                stmt.setString(1, adherent.getNom());
                stmt.setString(2, adherent.getPrenom());
                stmt.setString(3, adherent.getEmail());
                stmt.setString(4, adherent.getTelephone());
                stmt.executeUpdate();
                
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        adherent.setId(generatedKeys.getInt(1));
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
     * Récupère tous les adhérents de la base de données
     * @return Une liste de tous les adhérents
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données
     */
    public List<Adherent> getAdherents() throws SQLException {
        List<Adherent> adherents = new ArrayList<>();
        String sql = "SELECT * FROM Adherents";
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Adherent adherent = new Adherent();
                adherent.setId(rs.getInt("id"));
                
                String nom = rs.getString("nom");
                if (nom != null) {
                    adherent.setNom(nom);
                }
                
                String prenom = rs.getString("prenom");
                if (prenom != null) {
                    adherent.setPrenom(prenom);
                }
                
                String email = rs.getString("email");
                if (email != null) {
                    adherent.setEmail(email);
                }
                
                String telephone = rs.getString("telephone");
                if (telephone != null) {
                    adherent.setTelephone(telephone);
                }
                
                adherents.add(adherent);
            }
        }
        return adherents;
    }

    /**
     * Récupère un adhérent par son identifiant
     * @param id L'identifiant de l'adhérent à récupérer
     * @return L'adhérent correspondant ou null si non trouvé
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données
     */
    public Adherent getAdherentById(int id) throws SQLException {
        String sql = "SELECT * FROM Adherents WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Adherent adherent = new Adherent();
                    adherent.setId(rs.getInt("id"));
                    
                    String nom = rs.getString("nom");
                    if (nom != null) {
                        adherent.setNom(nom);
                    }
                    
                    String prenom = rs.getString("prenom");
                    if (prenom != null) {
                        adherent.setPrenom(prenom);
                    }
                    
                    String email = rs.getString("email");
                    if (email != null) {
                        adherent.setEmail(email);
                    }
                    
                    String telephone = rs.getString("telephone");
                    if (telephone != null) {
                        adherent.setTelephone(telephone);
                    }
                    
                    return adherent;
                }
            }
        }
        return null;
    }

    // Read - Search by name
    public List<Adherent> rechercherParNom(String nom) throws SQLException {
        List<Adherent> adherents = new ArrayList<>();
        String sql = "SELECT * FROM Adherents WHERE nom LIKE ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nom + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Adherent adherent = new Adherent();
                    adherent.setId(rs.getInt("id"));
                    adherent.setNom(rs.getString("nom"));
                    adherent.setPrenom(rs.getString("prenom"));
                    adherent.setEmail(rs.getString("email"));
                    adherent.setTelephone(rs.getString("telephone"));
                    adherents.add(adherent);
                }
            }
        }
        return adherents;
    }

    // Read - Search by email
    public List<Adherent> rechercherParEmail(String email) throws SQLException {
        List<Adherent> adherents = new ArrayList<>();
        String sql = "SELECT * FROM Adherents WHERE email LIKE ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + email + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Adherent adherent = new Adherent();
                    adherent.setId(rs.getInt("id"));
                    adherent.setNom(rs.getString("nom"));
                    adherent.setPrenom(rs.getString("prenom"));
                    adherent.setEmail(rs.getString("email"));
                    adherent.setTelephone(rs.getString("telephone"));
                    adherents.add(adherent);
                }
            }
        }
        return adherents;
    }

    // Update
    public void modifierAdherent(Adherent adherent) throws SQLException, IllegalArgumentException {
        if (adherent == null) {
            throw new IllegalArgumentException("L'adhérent ne peut pas être null");
        }
        
        // Validation des données
        if (!org.example.gestionbibliotheque.util.ValidationUtil.estNomValide(adherent.getNom())) {
            throw new IllegalArgumentException("Le nom de l'adhérent n'est pas valide. Il doit contenir uniquement des lettres et des espaces.");
        }
        
        if (!org.example.gestionbibliotheque.util.ValidationUtil.estNomValide(adherent.getPrenom())) {
            throw new IllegalArgumentException("Le prénom de l'adhérent n'est pas valide. Il doit contenir uniquement des lettres et des espaces.");
        }
        
        if (!org.example.gestionbibliotheque.util.ValidationUtil.estEmailValide(adherent.getEmail())) {
            throw new IllegalArgumentException("L'email n'est pas valide. Veuillez saisir un email au format correct.");
        }
        
        if (!org.example.gestionbibliotheque.util.ValidationUtil.estTelephoneValide(adherent.getTelephone())) {
            throw new IllegalArgumentException("Le numéro de téléphone n'est pas valide. Il doit contenir exactement 8 chiffres.");
        }
        
        String sql = "UPDATE Adherents SET nom = ?, prenom = ?, email = ?, telephone = ? WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            try {
                stmt.setString(1, adherent.getNom());
                stmt.setString(2, adherent.getPrenom());
                stmt.setString(3, adherent.getEmail());
                stmt.setString(4, adherent.getTelephone());
                stmt.setInt(5, adherent.getId());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Adhérent non trouvé avec l'ID: " + adherent.getId());
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
    public void supprimerAdherent(int id) throws SQLException {
        String sql = "DELETE FROM Adherents WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            try {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Adhérent non trouvé avec l'ID: " + id);
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