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
    private Label generatedAddress, feedbackText, balanceLabel,currentAddressLabel;

    @FXML
    private Button createWalletButton, loadWalletButton, restoreWalletButton, generateAddressButton;

    @FXML
    private TextField seedField, walletFileField,recipientAddressField, amountField;;

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
                setFeedback("Veuillez entrer un chemin de fichier valide.", false);
                return;
            }

            // Récupérer l'utilisateur connecté
            Integer userId = SessionManager.getLoggedInUserId();
            if (userId == null) {
                setFeedback("Erreur : Utilisateur non connecté.", false);
                return;
            }

            // Créer une nouvelle wallet
            walletService.createWallet(walletFilePath, userId);

            // Récupérer les détails de la wallet
            String seedPhrase = walletService.getSeedPhrase();
            String currentAddress = walletService.getCurrentAddress();
            double initialBalance = 0.05; // Solde initial

            // Enregistrer les détails de la wallet dans la base de données
            walletService.saveWalletToDatabase(userId, seedPhrase, walletFilePath, currentAddress, initialBalance);

            // Charger la vue de confirmation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/walletConfirmation.fxml"));
            Stage confirmationStage = new Stage();
            confirmationStage.setTitle("Confirmation de création");
            confirmationStage.setScene(new Scene(loader.load()));

            // Passer les détails au contrôleur de confirmation
            WalletConfirmationController confirmationController = loader.getController();
            confirmationController.setWalletDetails(currentAddress, initialBalance, seedPhrase, walletFilePath);

            // Afficher la vue de confirmation
            confirmationStage.show();

            setFeedback("Portefeuille créé avec succès !", true);
            balanceLabel.setText("Solde : " + initialBalance + " BTC");
            currentAddressLabel.setText("Adresse : " + currentAddress);

        } catch (Exception e) {
            setFeedback("Erreur lors de la création du portefeuille : " + e.getMessage(), false);
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
            String currentAddress = walletService.getCurrentAddress();
            double balance = walletService.getWalletBalanceByUserId(SessionManager.getLoggedInUserId());
            outputArea.setText("Wallet chargée avec succès.\nAdresse : " + currentAddress + "\nSolde : " + balance + " BTC");
            balanceLabel.setText("Solde : " + balance + " BTC");
            currentAddressLabel.setText("Adresse : " + currentAddress);
        } catch (Exception e) {
            setFeedback("Erreur lors du chargement du portefeuille : " + e.getMessage(), false);
        }
    }
    @FXML
    public void sendBitcoin() {
        try {
            Integer userId = SessionManager.getLoggedInUserId();
            if (userId == null) {
                setFeedback("Erreur : Utilisateur non connecté.", false);
                return;
            }

            String recipientAddress = recipientAddressField.getText();
            double amount = Double.parseDouble(amountField.getText());

            if (walletService.sendTransaction(userId, recipientAddress, amount)) {
                setFeedback("Transaction réussie !", true);
                double updatedBalance = walletService.getWalletBalanceByUserId(userId);
                balanceLabel.setText("Solde : " + updatedBalance + " BTC");
            } else {
                setFeedback("Erreur lors de la transaction.", false);
            }
        } catch (Exception e) {
            setFeedback("Erreur lors de l'envoi : " + e.getMessage(), false);
        }
    }
    @FXML
    public void restoreWallet() {
        try {
            String seedPhrase = seedField.getText();
            long creationTime = System.currentTimeMillis() / 1000; // Exemple de timestamp
            walletService.restoreWalletFromSeed(seedPhrase, creationTime);
            String currentAddress = walletService.getCurrentAddress();
            outputArea.setText("Wallet restaurée avec succès.\nAdresse : " + currentAddress);
        } catch (Exception e) {
            setFeedback("Erreur lors de la restauration du portefeuille : " + e.getMessage(), false);
        }
    }

    @FXML
    public void generateNewAddress() {
        try {
            String newAddress = walletService.generateNewAddress();
            setFeedback("Nouvelle adresse générée : " + newAddress, true);
            currentAddressLabel.setText("Adresse : " + newAddress);
        } catch (Exception e) {
            setFeedback("Erreur lors de la génération de l'adresse : " + e.getMessage(), false);
        }
    }

    private void checkOrCreateWallet() {
        try {
            walletService.loadWallet("wallet-test.wallet");
            balanceLabel.setText("Solde : " + walletService.getWalletBalance());
        } catch (Exception e) {
            try {
                walletService.createWallet("wallet-test.wallet", SessionManager.getLoggedInUserId());
                balanceLabel.setText("Wallet créée. Adresse : " + walletService.generateNewAddress());
            } catch (Exception ex) {
                setFeedback("Erreur : " + ex.getMessage(), false);
            }
        }
    }
}