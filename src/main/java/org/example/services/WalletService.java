package org.example.services;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.DeterministicKeyChain;

import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;
import org.example.utils.DatabaseUtils;
import org.example.utils.EncryptionUtils;

import java.sql.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class WalletService {

    private final NetworkParameters params;
    private Wallet wallet;


    public WalletService() {
        // Use TestNet for development and testing
        this.params = TestNet3Params.get();
    }
    public void saveWalletToDatabase(int userId, String seedPhrase, String filePath) {
        if (walletExists(userId, filePath)) {
            System.err.println("A wallet already exists for this user or file path.");
            return;
        }
        String sql = "INSERT INTO wallets (user_id, seed_phrase, file_path) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, seedPhrase);
            stmt.setString(3, filePath);
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

        // Generate and display the seed phrase
        DeterministicSeed seed = wallet.getKeyChainSeed();
        List<String> mnemonic = seed.getMnemonicCode();
        String seedPhrase = String.join(" ", mnemonic);
        System.out.println("Seed Phrase: " + seedPhrase);

        // Encrypt the seed phrase
        String encryptedSeedPhrase = EncryptionUtils.encrypt(seedPhrase);

        // Save the wallet to a file
        File walletFile = new File(walletFilePath);
        wallet.saveToFile(walletFile);

        System.out.println("Wallet created at: " + walletFile.getAbsolutePath());

        // Save the wallet details to the database
        saveWalletToDatabase(userId, encryptedSeedPhrase, walletFilePath);
        //saveWalletToDatabase(userId, encryptedSeedPhrase, walletFilePath);
    }

    public String getWalletBalanceByUserId(int userId) {
        // Vérifiez si une wallet existe pour cet utilisateur
        if (!userHasWallet(userId)) {
            return "0 BTC (Aucune wallet)";
        }

        // Logique pour charger la wallet et retourner le solde
        try {
            loadWallet(getWalletFilePathByUserId(userId));
            return wallet.getBalance().toFriendlyString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors du chargement du solde.";
        }
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
        // Load the wallet from the specified file
        File walletFile = new File(walletFilePath);
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
    public String getCurrentAddress() {
        if (wallet != null) {
            return wallet.currentReceiveAddress().toString();
        }
        return "No address available.";
    }
}
