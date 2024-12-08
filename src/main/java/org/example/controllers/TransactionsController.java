package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.models.Transaction;
import org.example.services.WalletService;
import org.example.utils.SessionManager;

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
    public void goBackToDashboard() {
        // Logic to navigate back to wallet_dashboard.fxml
    }
}
