package org.example.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.utils.DatabaseUtils;

public class AuthService {

    private boolean isAuthenticated = false;

    /**
     * Authentifie un utilisateur en vérifiant les informations dans la base de données.
     */
    public boolean authenticate(String username, String password) {
        String query = "SELECT password_hash FROM users WHERE username = ?";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password_hash");
                    isAuthenticated = storedPassword.equals(password); // Remplacez par un hachage sécurisé (ex: BCrypt)
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAuthenticated;
    }

    /**
     * Inscrit un nouvel utilisateur dans la base de données.
     */
    public boolean register(String username, String password) {
        String query = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // Remplacez par un hachage sécurisé
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // L'inscription a échoué
        }
    }

    /**
     * Déconnecte l'utilisateur.
     */
    public void logout() {
        isAuthenticated = false;
    }

    /**
     * Vérifie si l'utilisateur est authentifié.
     */
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
}
