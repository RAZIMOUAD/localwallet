package org.example.services;

import org.example.utils.DatabaseUtils;
import org.example.utils.SessionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    /**
     * Registers a new user in the database.
     *
     * @param username The username to register.
     * @param password The plain-text password to hash and store.
     * @return True if registration was successful, false otherwise.
     */
    public boolean register(String username, String password) {
        String query = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            String hashedPassword = hashPassword(password); // Hash the password before storing
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) { // SQL state 23000 indicates a unique constraint violation
                System.err.println("Username is already taken: " + username);
            } else {
                System.err.println("Error during registration: " + e.getMessage());
            }
            return false; // Registration failed
        }
    }
    public int getUserIdByUsername(String username) {
        String query = "SELECT id FROM users WHERE username = ?";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user ID: " + e.getMessage());
        }
        return -1; // Return -1 if no user found
    }

    /**
     * Authenticates a user by checking credentials in the database.
     *
     * @param username The username provided by the user.
     * @param password The plain-text password provided by the user.
     * @return The user's ID if authentication is successful, or -1 if it fails.
     */
    public int authenticate(String username, String password) {
        String query = "SELECT id, password_hash FROM users WHERE username = ?";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPasswordHash = rs.getString("password_hash");
                    int userId = rs.getInt("id");

                    // Verify the password
                    if (verifyPassword(password, storedPasswordHash)) {
                        SessionManager.setLoggedInUserId(userId); // Store user ID in session
                        return userId;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during authentication: " + e.getMessage());
        }
        return -1; // Authentication failed
    }

    /**
     * Logs out the current user by clearing the session.
     */
    public void logout() {
        SessionManager.clearSession();
    }

    /**
     * Hashes a plain-text password securely.
     *
     * @param password The plain-text password.
     * @return The hashed password.
     */
    private String hashPassword(String password) {
        // Replace with a secure password hashing library like BCrypt
        return password; // Dummy implementation for now
    }

    /**
     * Verifies a plain-text password against a hashed password.
     *
     * @param password       The plain-text password.
     * @param hashedPassword The hashed password.
     * @return True if the password matches, false otherwise.
     */
    private boolean verifyPassword(String password, String hashedPassword) {
        // Replace with a secure password verification library
        return password.equals(hashedPassword); // Dummy implementation for now
    }
}
