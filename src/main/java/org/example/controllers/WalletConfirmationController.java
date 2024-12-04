package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;

import java.io.IOException;

public class WalletConfirmationController {
    @FXML
    private Label feedbackText;

    @FXML
    private Label walletAddressLabel;

    @FXML
    private Label walletBalanceLabel;

    @FXML
    private Label seedPhraseLabel;

    @FXML
    private Label filePathLabel;
    @FXML
    private TextArea outputArea;


    @FXML
    public void closeAndRedirectToLogin() {
        try {
            // Charger login.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Scene scene = new Scene(loader.load());

            // Obtenir la fenêtre actuelle et remplacer la scène
            Stage stage = (Stage) feedbackText.getScene().getWindow(); // Remplacez feedbackText par n'importe quel élément de la vue actuelle
            stage.setScene(scene);


            // Facultatif : Ajuster le titre de la fenêtre
            stage.setTitle("Login");

        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println("Error loading login view: " + e.getMessage());
        }
    }

    // Method to set wallet details
    public void setWalletDetails(String address, String balance, String seedPhrase, String filePath) {
        walletAddressLabel.setText("Adresse : " + address);
        walletBalanceLabel.setText("Solde : " + balance);
        seedPhraseLabel.setText("Phrase secrète : " + seedPhrase);
        filePathLabel.setText("Chemin du fichier : " + filePath);
    }
    @FXML
    public void copySeedPhrase() {
        // Vérifier que la phrase secrète est disponible
        String seedPhrase = seedPhraseLabel.getText();
        if (seedPhrase == null || seedPhrase.isEmpty()) {
            System.out.println("Aucune phrase secrète à copier.");
            return;
        }

        // Utiliser le presse-papiers JavaFX pour copier la phrase secrète
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(seedPhrase);
        clipboard.setContent(content);

        System.out.println("Phrase secrète copiée dans le presse-papiers !");
    }
}


