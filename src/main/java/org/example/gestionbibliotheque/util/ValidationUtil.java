package org.example.gestionbibliotheque.util;

import java.util.regex.Pattern;

/**
 * Classe utilitaire pour la validation des données saisies
 * Cette classe fournit des méthodes statiques pour valider différents types de données
 */
public class ValidationUtil {

    // Regex pour valider un email
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    // Regex pour valider les noms et prénoms (lettres et espaces uniquement)
    private static final Pattern NOM_PATTERN = 
        Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$");
    
    // Regex pour valider les titres (pas de symboles spéciaux)
    private static final Pattern TITRE_PATTERN = 
        Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ0-9\\s.,;:!?'\"()-]+$");
    
    // Regex pour valider les numéros de téléphone (exactement 8 chiffres)
    private static final Pattern TELEPHONE_PATTERN = 
        Pattern.compile("^\\d{8}$");
    
    /**
     * Valide un titre de livre (pas de symboles spéciaux)
     * @param titre Le titre à valider
     * @return true si le titre est valide, false sinon
     */
    public static boolean estTitreValide(String titre) {
        if (titre == null || titre.trim().isEmpty()) {
            return false;
        }
        return TITRE_PATTERN.matcher(titre).matches();
    }
    
    /**
     * Valide un nom ou prénom (lettres et espaces uniquement)
     * @param nom Le nom ou prénom à valider
     * @return true si le nom est valide, false sinon
     */
    public static boolean estNomValide(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return false;
        }
        return NOM_PATTERN.matcher(nom).matches();
    }
    
    /**
     * Valide un email
     * @param email L'email à valider
     * @return true si l'email est valide, false sinon
     */
    public static boolean estEmailValide(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Valide un numéro de téléphone (exactement 8 chiffres)
     * @param telephone Le numéro de téléphone à valider
     * @return true si le numéro est valide, false sinon
     */
    public static boolean estTelephoneValide(String telephone) {
        if (telephone == null || telephone.trim().isEmpty()) {
            return false;
        }
        return TELEPHONE_PATTERN.matcher(telephone).matches();
    }
    
    /**
     * Valide un nombre d'exemplaires (doit être un nombre positif)
     * @param nbExemplaires Le nombre d'exemplaires à valider
     * @return true si le nombre est valide, false sinon
     */
    public static boolean estNombreExemplairesValide(int nbExemplaires) {
        return nbExemplaires >= 0;
    }
}