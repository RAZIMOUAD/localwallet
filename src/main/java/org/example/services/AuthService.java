package org.example.services;

import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthService {

    private final Map<String, String> userDatabase = new HashMap<>();
    private boolean isAuthenticated = false;

    public AuthService() {
        // Initialiser un utilisateur de démonstration
        userDatabase.put("admin", "password123");
    }

    /**
     * Vérifie les informations d'identification de l'utilisateur.
     */
    public boolean authenticate(String username, String password) {
        String storedPassword = userDatabase.get(username);
        isAuthenticated = storedPassword != null && storedPassword.equals(password);
        return isAuthenticated;
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
    public boolean register(String username, String password) {
        if (userDatabase.containsKey(username)) {
            return false; // L'utilisateur existe déjà
        }
        userDatabase.put(username, password);
        return true; // Inscription réussie
    }
}
