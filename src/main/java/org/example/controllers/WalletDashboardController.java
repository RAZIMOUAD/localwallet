package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.services.WalletService;
import org.example.utils.SessionManager;

public class WalletDashboardController {

    @FXML
    private Label balanceLabel;

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
        String balance = walletService.getWalletBalanceByUserId(userId);
        balanceLabel.setText("Solde : " + balance);

        // Afficher le nom d'utilisateur (facultatif, si stocké dans la session)
        String username = SessionManager.getLoggedInUsername(); // Ajoutez cette méthode si nécessaire
        if (username != null) {
            usernameLabel.setText("Connecté en tant que : " + username);
        }
    }

    @FXML
    public void sendMoney() {
        System.out.println("Redirection vers l'écran d'envoi d'argent...");
        // Logique pour rediriger vers la vue d'envoi
    }

    @FXML
    public void viewTransactions() {
        System.out.println("Redirection vers l'écran des transactions...");
        // Logique pour afficher les transactions
    }

    @FXML
    public void logout() {
        SessionManager.clearSession();
        System.out.println("Déconnexion réussie.");
        // Rediriger vers l'écran de connexion
    }

    @FXML
    public void exitApp() {
        System.exit(0);
    }
}
