package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.services.WalletService;
import org.example.utils.SessionManager;

import java.io.OutputStream;
import java.io.PrintStream;

public class SendBitcoinController {

    @FXML
    private TextField recipientAddressField;

    @FXML
    private TextField amountField;

    @FXML
    private Label feedbackLabel;

    private final WalletService walletService = new WalletService();
    @FXML
    private TextArea logArea;
    @FXML
    public void initialize() {
        // Rediriger les System.out vers le TextArea logArea
        redirectSystemOutToTextArea(logArea);
    }

    private void redirectSystemOutToTextArea(TextArea textArea) {
        PrintStream ps = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                textArea.appendText(String.valueOf((char) b));
            }
        });

        // Rediriger System.out et System.err vers le TextArea
        System.setOut(ps);
        System.setErr(ps);
    }
    @FXML
    public void sendBitcoin() {
        try {
            // Récupérer l'ID de l'utilisateur connecté
            int senderId = SessionManager.getLoggedInUserId();
            if (senderId == 0) {
                feedbackLabel.setText("Erreur : Utilisateur non connecté.");
                feedbackLabel.setStyle("-fx-text-fill: red;");
                return;
            }
// Vérifier si le montant est valide
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    feedbackLabel.setText("Le montant doit être supérieur à 0.");
                    feedbackLabel.setStyle("-fx-text-fill: red;");
                    return;
                }
            } catch (NumberFormatException e) {
                feedbackLabel.setText("Veuillez entrer un montant valide.");
                feedbackLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            // Récupérer les données
            String recipientAddress = recipientAddressField.getText();
            double amount = Double.parseDouble(amountField.getText());

            // Vérifier si les champs sont vides
            if (recipientAddress.isEmpty() || amount <= 0) {
                feedbackLabel.setText("Veuillez entrer une adresse et un montant valide.");
                feedbackLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            // vérifier l'adresse du destinataire
            if (!walletService.isRecipientAddressValid(recipientAddress)) {
                feedbackLabel.setText("L'adresse du destinataire est invalide.");
                feedbackLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            // Vérifier si l'adresse du destinataire est valide
            if (recipientAddress == null || recipientAddress.trim().isEmpty()) {
                feedbackLabel.setText("Veuillez entrer une adresse de destinataire valide.");
                feedbackLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            // Vérifier si l'utilisateur a suffisamment de fonds
            if (!walletService.hasSufficientBalance(senderId, amount)) {
                feedbackLabel.setText("Solde insuffisant pour effectuer cette transaction.");
                feedbackLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            // Envoyer les fonds
            boolean success = walletService.sendTransaction(senderId, recipientAddress, amount);
            if (success) {
                feedbackLabel.setText("Transaction réussie !");
                feedbackLabel.setStyle("-fx-text-fill: green;");
            } else {
                feedbackLabel.setText("Erreur lors de la transaction.");
                feedbackLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Veuillez entrer un montant valide.");
            feedbackLabel.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            feedbackLabel.setText("Erreur : " + e.getMessage());
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void goBackToDashboard() {
        // Retour à la vue principale
        Stage stage = (Stage) feedbackLabel.getScene().getWindow();
        stage.close();
    }
}
