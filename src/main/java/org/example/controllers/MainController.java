package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.Main;
import org.example.services.AuthService;
import org.example.services.BitcoinService;

public class MainController {

    @FXML
    private Label balanceLabel;

    private final BitcoinService bitcoinService = new BitcoinService();

    @FXML
    public void initialize() {
        updateBalance();
    }

    @FXML
    public void updateBalance() {
        balanceLabel.setText("Solde : " + bitcoinService.getBalance() + " BTC");
    }

    @FXML
    public void logout(ActionEvent event) {
        // Appeler le service de déconnexion
        AuthService authService = new AuthService();
        authService.logout();

        // Charger l'écran de connexion
        Main.loadScene("/views/login.fxml");

    }


    @FXML
    public void handleExit(ActionEvent event) {
        // Fermer l'application
        System.out.println("Application fermée !");
        System.exit(0);
    }

    @FXML
    public void handleSendMoney(ActionEvent event) {
        // Redirection vers l'écran d'envoi de fonds
        System.out.println("Envoi de fonds !");
    }

    @FXML
    public void handleSettings(ActionEvent event) {
        // Redirection vers l'écran des paramètres
        System.out.println("Paramètres ouverts !");
    }

    @FXML
    public void handleViewTransactions(ActionEvent event) {
        // Affichage des transactions récentes
        System.out.println("Affichage des transactions récentes !");
    }


}
