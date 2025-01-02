package org.example.services;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.DeterministicKeyChain;

import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;
import org.example.models.Transaction;
import org.example.utils.DatabaseUtils;
import org.example.utils.EncryptionUtils;

import java.sql.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WalletService {

    private final NetworkParameters params;
    private Wallet wallet;


    public WalletService() {
        // Use TestNet for development and testing
        this.params = TestNet3Params.get();
    }
    public void saveWalletToDatabase(int userId, String seedPhrase, String filePath, String currentAddress, double initialBalance) {
        if (walletExists(userId, filePath)) {
            System.err.println("A wallet already exists for this user or file path.");
            return;
        }

        String sql = "INSERT INTO wallets (user_id, seed_phrase, file_path, current_address, balance) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, seedPhrase);
            stmt.setString(3, filePath);
            stmt.setString(4, currentAddress); // Ajouter l'adresse courante
            stmt.setDouble(5, initialBalance); // Ajouter la balance initiale

            stmt.executeUpdate();

            System.out.println("Wallet saved to the database successfully.");

        } catch (Exception e) {
            System.err.println("Error saving wallet to the database: " + e.getMessage());
        }
    }

    public void createWallet(String walletFilePath, int userId) throws Exception {
        if (walletExists(userId, walletFilePath)) {
            throw new Exception("A wallet already exists for this user or file path.");
        }
        // Create a new deterministic wallet
        this.wallet = Wallet.createDeterministic(params, Script.ScriptType.P2PKH);
       // Générer l'adresse courante
        String currentAddress = wallet.freshReceiveAddress().toString();
        // Generate and display the seed phrase
        DeterministicSeed seed = wallet.getKeyChainSeed();
        List<String> mnemonic = seed.getMnemonicCode();
        String seedPhrase = String.join(" ", mnemonic);
        System.out.println("Seed Phrase: " + seedPhrase);
        // Définir une balance initiale
        double initialBalance = 0.05; // Exemple : 0.05 BTC
        // Encrypt the seed phrase
        String encryptedSeedPhrase = EncryptionUtils.encrypt(seedPhrase);
        // Save the wallet to a file
        File walletFile = new File(walletFilePath);
        wallet.saveToFile(walletFile);
        System.out.println("Wallet created at: " + walletFile.getAbsolutePath());
        // Save the wallet details to the database
        saveWalletToDatabase(userId, encryptedSeedPhrase, walletFilePath, currentAddress, initialBalance);
        //saveWalletToDatabase(userId, encryptedSeedPhrase, walletFilePath);
    }

    public double getWalletBalanceByUserId(int userId) {
        String query = "SELECT balance FROM wallets WHERE user_id = ?";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("balance");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0; // Retourne 0.0 si aucune wallet n'est trouvée ou en cas d'erreur
    }



    public boolean userHasWallet(int userId) {
        return walletExists(userId, null); // Pass null for filePath
    }

    private String getWalletFilePathByUserId(int userId) {
        String query = "SELECT file_path FROM wallets WHERE user_id = ?";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("file_path");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadWallet(String walletFilePath) throws Exception {
        if (walletFilePath == null || walletFilePath.isEmpty()) {
            throw new Exception("Invalid wallet file path.");
        }

        File walletFile = new File(walletFilePath);
        if (!walletFile.exists()) {
            throw new Exception("Wallet file not found.");
        }

        this.wallet = Wallet.loadFromFile(walletFile);

        System.out.println("Wallet loaded!");
        System.out.println("Current address: " + wallet.currentReceiveAddress());
    }

    public boolean walletExists(int userId, String filePath) {
        String query = "SELECT COUNT(*) FROM wallets WHERE user_id = ? OR file_path = ?";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setString(2, filePath);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Returns true if a wallet exists
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void restoreWalletFromSeed(String seedPhrase, long creationTime) {
        try {
            // Convert the seed phrase into a list of strings
            List<String> mnemonic = Arrays.asList(seedPhrase.split(" "));
            DeterministicSeed seed = new DeterministicSeed(mnemonic, null, "", creationTime);

            // Restore the wallet from the seed
            this.wallet = Wallet.fromSeed(params, seed, Script.ScriptType.P2PKH);

            System.out.println("Wallet restored!");
            System.out.println("Address: " + wallet.currentReceiveAddress());
        } catch (Exception e) {
            System.err.println("Error restoring wallet: " + e.getMessage());
        }
    }

    public String getWalletBalance() {
        return wallet != null ? "Wallet balance: " + wallet.getBalance().toFriendlyString() : "No wallet loaded.";
    }

    public String generateNewAddress() {
        return wallet != null ? "New address: " + wallet.freshReceiveAddress() : "No wallet loaded.";
    }
    public String getSeedPhrase() {
        if (wallet != null) {
            DeterministicSeed seed = wallet.getKeyChainSeed();
            return String.join(" ", seed.getMnemonicCode());
        }
        return "No wallet loaded.";
    }
    public String getCurrentAddressByUserId(int userId) {
        String query = "SELECT current_address FROM wallets WHERE user_id = ?";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("current_address");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No address available.";
    }
    public String getCurrentAddress() {
        if (wallet != null) {
            return wallet.currentReceiveAddress().toString(); // Adresse actuelle de réception
        }
        return "Adresse non disponible."; // Message par défaut si le portefeuille n'est pas chargé
    }
    public boolean sendTransaction(int senderId, String recipientAddress, double amount) {
        String getSenderWalletQuery = "SELECT id FROM wallets WHERE user_id = ?";
        String deductBalanceQuery = "UPDATE wallets SET balance = balance - ? WHERE user_id = ?";
        String addBalanceQuery = "UPDATE wallets SET balance = balance + ? WHERE current_address = ?";
        String insertTransactionQuery = "INSERT INTO transactions (sender_wallet_id, receiver_wallet_address, amount) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseUtils.getConnection()) {
            connection.setAutoCommit(false);
        // Get the sender wallet ID
            int senderWalletId = -1;
            try (PreparedStatement getWalletStmt = connection.prepareStatement(getSenderWalletQuery)) {
                getWalletStmt.setInt(1, senderId);
                try (ResultSet rs = getWalletStmt.executeQuery()) {
                    if (rs.next()) {
                        senderWalletId = rs.getInt("id");
                    } else {
                        throw new Exception("Sender wallet not found.");
                    }
                }
            }
            // Debug the retrieved wallet ID
            System.out.println("Sender Wallet ID: " + senderWalletId);
            // Vérifier si l'utilisateur a suffisamment de fonds
            if (!hasSufficientBalance(senderId, amount)) {
                throw new Exception("Insufficient balance.");
            }
            System.out.println("Suffisamment de fonds pour l'utilisateur : " + senderId);
            // Debug the recipient address and amount
            System.out.println("Recipient Address: " + recipientAddress);
            System.out.println("Amount: " + amount);

            // Déduire du portefeuille de l'expéditeur
            try (PreparedStatement deductStmt = connection.prepareStatement(deductBalanceQuery)) {
                deductStmt.setDouble(1, amount);
                deductStmt.setInt(2, senderId);
                int rowsAffected = deductStmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new Exception("Failed to deduct balance from sender's wallet.");
                }
                System.out.println("Rows affected for deduction: " + rowsAffected);
            }

            // Ajouter au portefeuille du destinataire
            try (PreparedStatement addStmt = connection.prepareStatement(addBalanceQuery)) {
                addStmt.setDouble(1, amount);
                addStmt.setString(2, recipientAddress);
                int rowsAffected = addStmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new Exception("Failed to add balance to recipient's wallet.");
                }
                System.out.println("Rows affected for addition: " + rowsAffected);
            }

            // Insérer la transaction
            try (PreparedStatement transactionStmt = connection.prepareStatement(insertTransactionQuery)) {
                transactionStmt.setInt(1, senderWalletId);
                transactionStmt.setString(2, recipientAddress);
                transactionStmt.setDouble(3, amount);
                int rowsAffected = transactionStmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new Exception("Failed to insert transaction.");
                }
                System.out.println("Transaction inserted, rows affected: " + rowsAffected);
            }

            connection.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean hasSufficientBalance(int userId, double amount) {
        String query = "SELECT balance FROM wallets WHERE user_id = ?";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double currentBalance = rs.getDouble("balance");
                    return currentBalance >= amount; // Vérifie si le solde est suffisant
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retourne false en cas d'erreur ou si le solde est insuffisant

    }
    public boolean isRecipientAddressValid(String recipientAddress) {
        String query = "SELECT COUNT(*) FROM wallets WHERE current_address = ?";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, recipientAddress);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Retourne true si l'adresse existe
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Retourne false si l'adresse n'existe pas
    }
    public List<Transaction> getTransactionsByUserId(int userId) {
        String getWalletIdQuery = "SELECT id FROM wallets WHERE user_id = ?";
        String getTransactionsQuery = "SELECT t.id, t.sender_wallet_id, t.receiver_wallet_address, t.amount, t.created_at " +
                "FROM transactions t " +
                "WHERE t.sender_wallet_id = ? OR t.receiver_wallet_address IN " +
                "(SELECT current_address FROM wallets WHERE user_id = ?)";

        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement walletStmt = connection.prepareStatement(getWalletIdQuery)) {

            // Get wallet IDs for the user
            walletStmt.setInt(1, userId);
            try (ResultSet walletRs = walletStmt.executeQuery()) {
                while (walletRs.next()) {
                    int walletId = walletRs.getInt("id");
                    System.out.println("Wallet ID found for user: " + walletId);

                    // Fetch transactions related to this wallet ID
                    try (PreparedStatement transactionStmt = connection.prepareStatement(getTransactionsQuery)) {
                        transactionStmt.setInt(1, walletId);
                        transactionStmt.setInt(2, userId);

                        try (ResultSet transactionRs = transactionStmt.executeQuery()) {
                            while (transactionRs.next()) {
                                Transaction transaction = new Transaction(
                                        transactionRs.getInt("id"),
                                        transactionRs.getInt("sender_wallet_id"),
                                        transactionRs.getString("receiver_wallet_address"),
                                        transactionRs.getDouble("amount"),
                                        transactionRs.getTimestamp("created_at").toLocalDateTime()
                                );
                                transactions.add(transaction);

                                System.out.println("Transaction found: " + transaction);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return transactions;
    }



}
