package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.services.BitcoinService;

public class WalletController {

    @FXML
    private TextField amountField;

    @FXML
    private TextField addressField;

    private final BitcoinService bitcoinService = new BitcoinService();

    @FXML
    public void sendMoney(ActionEvent event) {
        String address = addressField.getText();
        String amount = amountField.getText();

        if (bitcoinService.sendMoney(address, amount)) {
            System.out.println("Money sent successfully!");
        } else {
            System.out.println("Failed to send money.");
        }
    }

    @FXML
    public void requestMoney(ActionEvent event) {
        String receiveAddress = bitcoinService.getReceiveAddress();
        System.out.println("Share this address to receive funds: " + receiveAddress);
    }

    @FXML
    public void handleChangePassword(ActionEvent event) {
        // Logique pour changer le mot de passe
        System.out.println("Password change functionality triggered.");
    }

    @FXML
    public void handleShowPrivateKey(ActionEvent event) {
        // Logique pour afficher la clé privée
        String privateKey = bitcoinService.getPrivateKey();
        System.out.println("Private Key: " + privateKey);
    }

    @FXML
    public void handleBackToMain(ActionEvent event) {
        // Logique pour retourner à l'écran principal
        System.out.println("Returning to the main screen.");
    }
}
