package org.example.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.utils.DatabaseUtils;

public class AuthService {

    private boolean isAuthenticated = false;

    /**
     * Registers a new user in the database.
     */
    public boolean register(String username, String password) {
        String query = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // Replace with hashed password in production
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error during registration: " + e.getMessage());
            return false; // Registration failed
        }
    }

    /**
     * Authenticates a user by checking credentials in the database.
     */
    public boolean authenticate(String username, String password) {
        String query = "SELECT password_hash FROM users WHERE username = ?";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password_hash");
                    // Replace with secure password verification in production (e.g., BCrypt)
                    isAuthenticated = storedPassword.equals(password);
                    return isAuthenticated;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during authentication: " + e.getMessage());
        }
        return false; // Authentication failed
    }

    /**
     * Logs out the user.
     */
    public void logout() {
        isAuthenticated = false;
    }

    /**
     * Checks if the user is authenticated.
     */
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
}
