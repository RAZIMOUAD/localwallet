package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.models.Transaction;
import org.example.services.WalletService;
import org.example.utils.SessionManager;

import java.io.IOException;
import java.util.List;

public class TransactionsController {

    @FXML
    private TableView<Transaction> transactionsTable;

    @FXML
    private TableColumn<Transaction, String> dateColumn;

    @FXML
    private TableColumn<Transaction, Double> amountColumn;

    @FXML
    private TableColumn<Transaction, String> addressColumn;

    private final WalletService walletService = new WalletService();

    @FXML
    public void initialize() {
        // Set up table columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("receiverWalletAddress"));

        // Load transaction data
        loadTransactions();
    }

    private void loadTransactions() {
        int userId = SessionManager.getLoggedInUserId();
        if (userId == 0) {
            System.out.println("No user is logged in.");
            return;
        }

        // Fetch transactions for the user's wallets
        List<Transaction> transactions = walletService.getTransactionsByUserId(userId);
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for user: " + userId);
        } else {
            System.out.println("Transactions loaded: " + transactions.size());
        }

        ObservableList<Transaction> transactionList = FXCollections.observableArrayList(transactions);
        transactionsTable.setItems(transactionList);
    }

    @FXML
    public void goBackToWalletDashboard(ActionEvent event) {
        try {
            // Load the Wallet_Dashboard.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Wallet_Dashboard.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event source (button)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene for the Wallet Dashboard
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Wallet Dashboard");

            // Show the updated stage (this replaces the current window)
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading the Wallet Dashboard page: " + e.getMessage());
        }
    }


}

