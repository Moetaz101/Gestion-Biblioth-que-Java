package org.example.gestionbibliotheque.dao;

import org.example.gestionbibliotheque.model.Emprunt;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe d'accès aux données pour les emprunts
 * Cette classe gère toutes les opérations de base de données liées aux emprunts
 */
public class EmpruntDAO {
    public EmpruntDAO() {
        // No need to instantiate Database
    }

    /**
     * Crée un nouvel emprunt dans la base de données
     * @param emprunt L'emprunt à créer
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données
     */
    public void ajouterEmprunt(Emprunt emprunt) throws SQLException {
        String sql = "INSERT INTO Emprunts (date_emprunt, livre_id, adherent_id, date_retour_prevue) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDate(1, java.sql.Date.valueOf(emprunt.getDateEmprunt()));
            pstmt.setInt(2, emprunt.getLivreId());
            pstmt.setInt(3, emprunt.getAdherentId());
            pstmt.setDate(4, java.sql.Date.valueOf(emprunt.getDateRetourPrevue()));
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    emprunt.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    /**
     * Récupère tous les emprunts de la base de données
     * @return Une liste de tous les emprunts
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données
     */
    /**
     * Récupère tous les emprunts de la base de données
     * @return Une liste de tous les emprunts
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données
     */
    public List<Emprunt> getEmprunts() throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM Emprunts";
        
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Emprunt emprunt = new Emprunt();
                emprunt.setId(rs.getInt("id"));
                emprunt.setLivreId(rs.getInt("livre_id"));
                emprunt.setAdherentId(rs.getInt("adherent_id"));
                
                Date dateEmprunt = rs.getDate("date_emprunt");
                if (dateEmprunt != null) {
                    emprunt.setDateEmprunt(dateEmprunt.toLocalDate());
                }
                
                Date dateRetourPrevue = rs.getDate("date_retour_prevue");
                if (dateRetourPrevue != null) {
                    emprunt.setDateRetourPrevue(dateRetourPrevue.toLocalDate());
                }
                
                Date dateRetourEffective = rs.getDate("date_retour_effective");
                if (dateRetourEffective != null) {
                    emprunt.setDateRetourEffective(dateRetourEffective.toLocalDate());
                }
                
                emprunts.add(emprunt);
            }
        }
        
        return emprunts;
    }

    /**
     * Marque un livre comme retourné en mettant à jour sa date de retour
     * @param empruntId L'identifiant de l'emprunt à retourner
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données
     */
    /**
     * Marque un livre comme retourné en mettant à jour sa date de retour effective
     * @param empruntId L'identifiant de l'emprunt à retourner
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données
     */
    public void retournerLivre(int empruntId) throws SQLException {
        String sql = "UPDATE Emprunts SET date_retour_effective = ? WHERE id = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            try {
                pstmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
                pstmt.setInt(2, empruntId);
                
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Emprunt non trouvé avec l'ID: " + empruntId);
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    /**
     * Récupère tous les emprunts en cours (non retournés)
     * @return Une liste des emprunts en cours
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données
     */
    public List<Emprunt> getEmpruntsEnCours() throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM Emprunts WHERE date_retour_effective IS NULL";
        
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Emprunt emprunt = new Emprunt();
                emprunt.setId(rs.getInt("id"));
                emprunt.setLivreId(rs.getInt("livre_id"));
                emprunt.setAdherentId(rs.getInt("adherent_id"));
                emprunt.setDateEmprunt(rs.getDate("date_emprunt").toLocalDate());
                emprunt.setDateRetourPrevue(rs.getDate("date_retour_prevue").toLocalDate());
                Date dateRetourEffective = rs.getDate("date_retour_effective");
                if (dateRetourEffective != null) {
                    emprunt.setDateRetourEffective(dateRetourEffective.toLocalDate());
                }
                emprunts.add(emprunt);
            }
        }
        
        return emprunts;
    }

    /**
     * Récupère un emprunt par son identifiant
     * @param id L'identifiant de l'emprunt à récupérer
     * @return L'emprunt trouvé ou null si aucun emprunt n'est trouvé
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données
     */
    public Emprunt getEmpruntById(int id) throws SQLException {
        String sql = "SELECT * FROM Emprunts WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Emprunt emprunt = new Emprunt();
                    emprunt.setId(rs.getInt("id"));
                    emprunt.setLivreId(rs.getInt("livre_id"));
                    emprunt.setAdherentId(rs.getInt("adherent_id"));
                    emprunt.setDateEmprunt(rs.getDate("date_emprunt").toLocalDate());
                    emprunt.setDateRetourPrevue(rs.getDate("date_retour_prevue").toLocalDate());
                    Date dateRetourEffective = rs.getDate("date_retour_effective");
                    if (dateRetourEffective != null) {
                        emprunt.setDateRetourEffective(dateRetourEffective.toLocalDate());
                    }
                    return emprunt;
                }
            }
        }
        return null;
    }

    /**
     * Recherche les emprunts par l'identifiant du livre
     * @param livreId L'identifiant du livre
     * @return Une liste des emprunts trouvés
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données
     */
    /**
     * Recherche les emprunts par identifiant de livre
     * @param livreId L'identifiant du livre à rechercher
     * @return Une liste des emprunts correspondant au livre
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données
     */
    public List<Emprunt> rechercherParLivreId(int livreId) throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM Emprunts WHERE livre_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, livreId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Emprunt emprunt = new Emprunt();
                    emprunt.setId(rs.getInt("id"));
                    emprunt.setLivreId(rs.getInt("livre_id"));
                    emprunt.setAdherentId(rs.getInt("adherent_id"));
                    
                    Date dateEmprunt = rs.getDate("date_emprunt");
                    if (dateEmprunt != null) {
                        emprunt.setDateEmprunt(dateEmprunt.toLocalDate());
                    }
                    
                    Date dateRetourPrevue = rs.getDate("date_retour_prevue");
                    if (dateRetourPrevue != null) {
                        emprunt.setDateRetourPrevue(dateRetourPrevue.toLocalDate());
                    }
                    
                    Date dateRetourEffective = rs.getDate("date_retour_effective");
                    if (dateRetourEffective != null) {
                        emprunt.setDateRetourEffective(dateRetourEffective.toLocalDate());
                    }
                    
                    emprunts.add(emprunt);
                }
            }
        }
        return emprunts;
    }

    /**
     * Recherche les emprunts par l'identifiant de l'adhérent
     * @param adherentId L'identifiant de l'adhérent
     * @return Une liste des emprunts trouvés
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données
     */
    public List<Emprunt> rechercherParAdherentId(int adherentId) throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM Emprunts WHERE adherent_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, adherentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Emprunt emprunt = new Emprunt();
                    emprunt.setId(rs.getInt("id"));
                    emprunt.setLivreId(rs.getInt("livre_id"));
                    emprunt.setAdherentId(rs.getInt("adherent_id"));
                    emprunt.setDateEmprunt(rs.getDate("date_emprunt").toLocalDate());
                    emprunt.setDateRetourPrevue(rs.getDate("date_retour_prevue").toLocalDate());
                    Date dateRetourEffective = rs.getDate("date_retour_effective");
                    if (dateRetourEffective != null) {
                        emprunt.setDateRetourEffective(dateRetourEffective.toLocalDate());
                    }
                    emprunts.add(emprunt);
                }
            }
        }
        return emprunts;
    }

    /**
     * Modifie un emprunt existant
     * @param emprunt L'emprunt à modifier
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données
     */
    public void modifierEmprunt(Emprunt emprunt) throws SQLException {
        String sql = "UPDATE Emprunts SET livre_id = ?, adherent_id = ?, date_emprunt = ?, date_retour_prevue = ?, date_retour_effective = ? WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            try {
                stmt.setInt(1, emprunt.getLivreId());
                stmt.setInt(2, emprunt.getAdherentId());
                stmt.setDate(3, Date.valueOf(emprunt.getDateEmprunt()));
                stmt.setDate(4, Date.valueOf(emprunt.getDateRetourPrevue()));
                stmt.setDate(5, emprunt.getDateRetourEffective() != null ? Date.valueOf(emprunt.getDateRetourEffective()) : null);
                stmt.setInt(6, emprunt.getId());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Emprunt non trouvé avec l'ID: " + emprunt.getId());
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
     * Supprime un emprunt
     * @param id L'identifiant de l'emprunt à supprimer
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données
     */
    public void supprimerEmprunt(int id) throws SQLException {
        String sql = "DELETE FROM Emprunts WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            try {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Emprunt non trouvé avec l'ID: " + id);
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