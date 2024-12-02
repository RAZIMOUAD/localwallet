package org.example.services;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BitcoinService {

    private double balance = 1.234; // Solde initial fictif
    private final List<String> transactionHistory = new ArrayList<>(); // Historique des transactions
    private final SecureRandom secureRandom = new SecureRandom(); // Génération sécurisée d'adresses

    /**
     * Renvoie le solde actuel du portefeuille.
     *
     * @return Solde du portefeuille en BTC.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Simule l'envoi d'argent à une adresse.
     *
     * @param address Adresse Bitcoin de destination.
     * @param amount  Montant à envoyer.
     * @return true si la transaction est réussie, sinon false.
     */
    public boolean sendMoney(String address, String amount) {
        try {
            double amountToSend = Double.parseDouble(amount);

            // Vérification des conditions
            if (amountToSend <= 0) {
                System.out.println("Invalid amount. Must be greater than 0.");
                return false;
            }

            if (amountToSend > balance) {
                System.out.println("Insufficient balance.");
                return false;
            }

            // Mise à jour du solde et enregistrement de la transaction
            balance -= amountToSend;
            String transaction = "Sent " + amountToSend + " BTC to " + address;
            transactionHistory.add(transaction);

            System.out.println(transaction);
            return true;

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format. Please use a valid number.");
            return false;
        }
    }

    /**
     * Génère une adresse Bitcoin pour recevoir des fonds.
     *
     * @return Adresse Bitcoin générée.
     */
    public String getReceiveAddress() {
        // Exemple simplifié d'une génération d'adresse Bitcoin fictive
        String address = "1BTC" + secureRandom.nextInt(1_000_000);
        System.out.println("Generated receive address: " + address);
        return address;
    }

    /**
     * Récupère l'historique des transactions.
     *
     * @return Liste des transactions sous forme de chaîne de caractères.
     */
    public List<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory); // Renvoie une copie pour éviter les modifications externes
    }

    /**
     * Simule la récupération d'une clé privée associée au portefeuille.
     *
     * @return Clé privée fictive.
     */
    public String getPrivateKey() {
        // Exemple d'une clé privée fictive (ne pas utiliser en production)
        String privateKey = "L1aW4aubDFB7yfras2S1mME5gS6dzrL9zs8PHUX2T5zVWmP5fgU3";
        System.out.println("Retrieved private key: " + privateKey);
        return privateKey;
    }

    /**
     * Ajoute des fonds au portefeuille pour des tests ou une utilisation fictive.
     *
     * @param amount Montant à ajouter.
     */
    public void addFunds(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Added " + amount + " BTC to the wallet. New balance: " + balance + " BTC");
        } else {
            System.out.println("Amount to add must be greater than 0.");
        }
    }
}
