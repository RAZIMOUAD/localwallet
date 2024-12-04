package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.Main;
import org.example.services.AuthService;
import org.example.services.WalletService;
import org.example.utils.SessionManager;

import java.io.IOException;

public class AuthController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label feedbackText;

    private final AuthService authService = new AuthService();

    @FXML
    public void login(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        // Authenticate the user
        int userId = authService.authenticate(username, password);
        if (userId > 0) {
            SessionManager.setLoggedInUserId(userId);
            SessionManager.setLoggedInUsername(username); // Save the username in session

            feedbackText.setText("Login successful!");
            feedbackText.setStyle("-fx-fill: green;");
            redirectToWalletDashboard(); // Redirect to wallet view after successful login
        } else {
            feedbackText.setText("Invalid credentials. Please try again.");
            feedbackText.setStyle("-fx-fill: red;");
        }
    }
    @FXML
    private void redirectToWalletDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/wallet_dashboard.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
            stage.setTitle("Tableau de bord de la Wallet");
        } catch (IOException e) {
            e.printStackTrace();
            feedbackText.setText("Erreur lors du chargement du tableau de bord.");
            feedbackText.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void goToRegister(ActionEvent event) {
        Main.loadScene("/views/register.fxml");
    }

    /*private void redirectToWallet() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(loader.load());

            // Pass the WalletService instance to WalletController
            WalletController walletController = loader.getController();
            walletController.setWalletService(new WalletService());

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            feedbackText.setText("Error loading wallet dashboard.");
            feedbackText.setStyle("-fx-fill: red;");
        }
    }*/

}
