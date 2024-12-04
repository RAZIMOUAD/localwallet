package org.example.controllers;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.services.WalletService;
import javafx.stage.Stage;
import org.example.utils.SessionManager;

public class WalletController {

    public Label walletDetails;
    @FXML
    private Label generatedAddress, feedbackText, balanceLabel;

    @FXML
    private Button createWalletButton, loadWalletButton, restoreWalletButton, generateAddressButton;

    @FXML
    private TextField seedField, walletFileField;

    @FXML
    private TextArea outputArea;

    private WalletService walletService;

    private void setFeedback(String message, boolean isSuccess) {
        feedbackText.setText(message);
        feedbackText.setStyle(isSuccess ? "-fx-text-fill: green;" : "-fx-text-fill: #7d6060;");
    }

    public void setWalletService(WalletService walletService) {
        this.walletService = walletService;
        checkOrCreateWallet(); // Automatically check or create a wallet when injected
    }

    @FXML
    public void createWallet() {
        try {
            String walletFilePath = walletFileField.getText();
            if (walletFilePath == null || walletFilePath.trim().isEmpty()) {
                feedbackText.setText("Veuillez entrer un chemin de fichier valide.");
                feedbackText.setStyle("-fx-text-fill: red;");
                return;
            }
            // Get the logged-in user ID from the session
            Integer userId = SessionManager.getLoggedInUserId();
            if (userId == null) {
                feedbackText.setText("Erreur : Utilisateur non connecté.");
                feedbackText.setStyle("-fx-text-fill: red;");
                return;
            }

            walletService.createWallet(walletFilePath, userId);

            // Generate wallet details
            String seedPhrase = walletService.getSeedPhrase();
            String address = walletService.getCurrentAddress();
            String walletBalance = walletService.getWalletBalance();
// Save wallet details to the database
            walletService.saveWalletToDatabase(userId, seedPhrase, walletFilePath);

            // Load confirmation view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/walletConfirmation.fxml"));
            Stage confirmationStage = new Stage();
            confirmationStage.setTitle("Confirmation de création");
            confirmationStage.setScene(new Scene(loader.load()));

            // Pass wallet details to the confirmation controller
            WalletConfirmationController confirmationController = loader.getController();
            confirmationController.setWalletDetails(address , walletBalance, seedPhrase, walletFilePath);


            // Show confirmation view
            confirmationStage.show();

            // Update feedback
            feedbackText.setText("Portefeuille créé avec succès !");
            feedbackText.setStyle("-fx-text-fill: green;");

        } catch (Exception e) {
            feedbackText.setText("Erreur lors de la création du portefeuille : " + e.getMessage());
            feedbackText.setStyle("-fx-text-fill: red;");
        }
    }
    private int getCurrentUserId() {
        // Replace with actual logic to retrieve the logged-in user ID
        return 1; // Placeholder for testing
    }
    @FXML
    public void loadWallet() {
        try {
            String walletFilePath = walletFileField.getText();
            walletService.loadWallet(walletFilePath);
            outputArea.setText("Wallet loaded successfully.\nBalance: " + walletService.getWalletBalance());
        } catch (Exception e) {
            outputArea.setText("Error loading wallet: " + e.getMessage());
        }
    }

    @FXML
    public void restoreWallet() {
        try {
            String seedPhrase = seedField.getText();
            long creationTime = System.currentTimeMillis() / 1000; // Example creation time
            walletService.restoreWalletFromSeed(seedPhrase, creationTime);
            outputArea.setText("Wallet restored successfully.\nAddress: " + walletService.generateNewAddress());
        } catch (Exception e) {
            outputArea.setText("Error restoring wallet: " + e.getMessage());
        }
    }

    @FXML
    public void generateNewAddress() {
        try {
            String newAddress = walletService.generateNewAddress();
            outputArea.setText("New address generated: " + newAddress);
        } catch (Exception e) {
            outputArea.setText("Error generating address: " + e.getMessage());
        }
    }

    private void checkOrCreateWallet() {
        try {
            walletService.loadWallet("wallet-test.wallet");
            balanceLabel.setText("Solde : " + walletService.getWalletBalance());
        } catch (Exception e) {
            try {
                walletService.createWallet("wallet-test.wallet",getCurrentUserId());
                balanceLabel.setText("Wallet created. Address: " + walletService.generateNewAddress());
            } catch (Exception ex) {
                feedbackText.setText("Erreur : " + ex.getMessage());
            }
        }
    }
}
