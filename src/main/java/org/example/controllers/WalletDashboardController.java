package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import org.example.Main;
import org.example.services.AuthService;
import org.example.services.WalletService;
import org.example.utils.SessionManager;

import java.io.IOException;

public class WalletDashboardController {

    @FXML
    private Label balanceLabel;
    @FXML
    private Label currentAddressLabel;
    @FXML
    private Label usernameLabel;

    private final WalletService walletService = new WalletService();

    @FXML
    public void initialize() {
        // Charger les informations de l'utilisateur connecté
        Integer userId = SessionManager.getLoggedInUserId();
        if (userId == null) {
            balanceLabel.setText("Erreur : Utilisateur non connecté.");
            return;
        }

        // Charger le solde de la wallet
        double balance = walletService.getWalletBalanceByUserId(userId);
        balanceLabel.setText("Solde : " + balance);
// Charger l'adresse actuelle
        String currentAddress = walletService.getCurrentAddressByUserId(userId);
        currentAddressLabel.setText("Adresse de réception : " + currentAddress);
        // Afficher le nom d'utilisateur (facultatif, si stocké dans la session)
        String username = SessionManager.getLoggedInUsername(); // Ajoutez cette méthode si nécessaire
        if (username != null) {
            usernameLabel.setText("Connecté en tant que : " + username);
        }
    }
    @FXML
    public void copyAddress() {
        try {
            String address = currentAddressLabel.getText().replace("Adresse de réception : ", "");
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(address);
            clipboard.setContent(content);
            currentAddressLabel.setText("Adresse copiée !");
        } catch (Exception e) {
            currentAddressLabel.setText("Erreur lors de la copie de l'adresse.");
        }
    }
    @FXML
    public void sendMoney(ActionEvent event) {
        try {
            // Load the Send_Bitcoin.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/send_Bitcoin.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event source (button)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene for Send_Bitcoin
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Send Bitcoin");

            // Show the updated stage
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading the Send Bitcoin page: " + e.getMessage());
        }
    }


    @FXML
    public void viewTransactions(ActionEvent event) {
        try {
            // Load the Send_Bitcoin.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/transactions.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event source (button)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene for Send_Bitcoin
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Send Bitcoin");

            // Show the updated stage
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading the Send Bitcoin page: " + e.getMessage());
        }
    }

    @FXML
    public void logout() {
        // Appeler le service de déconnexion
        AuthService authService = new AuthService();
        authService.logout();

        // Charger l'écran de connexion
        Main.loadScene("/views/login.fxml");
        SessionManager.clearSession();
        System.out.println("Déconnexion réussie.");
        // Rediriger vers l'écran de connexion
    }

    @FXML
    public void exitApp() {
        System.exit(0);
    }
    @FXML
    public void refreshBalance() {
        try {
            // Get the logged-in user ID
            int userId = SessionManager.getLoggedInUserId();
            if (userId != 0) {
                double newBalance = walletService.getWalletBalanceByUserId(userId);
                balanceLabel.setText("Solde : " + String.format("%.8f BTC", newBalance));
            } else {
                balanceLabel.setText("Erreur : Utilisateur non connecté.");
            }
        } catch (Exception e) {
            balanceLabel.setText("Erreur lors de la récupération du solde.");
            e.printStackTrace();
        }
    }
}
