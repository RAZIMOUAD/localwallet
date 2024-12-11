package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    public void sendMoney() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/send_bitcoin.fxml"));
            Stage sendBitcoinStage = new Stage();
            sendBitcoinStage.setTitle("Envoyer des Bitcoins");
            sendBitcoinStage.setScene(new Scene(loader.load()));
            sendBitcoinStage.show();
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de la vue d'envoi : " + e.getMessage());
        }
    }

    @FXML
    public void viewTransactions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/transactions.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Historique des Transactions");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
}
