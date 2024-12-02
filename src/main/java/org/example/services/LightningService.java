package org.example.services;

public class LightningService {

    /**
     * Simule l'envoi de satoshis via le Lightning Network.
     */
    public boolean sendSats(String paymentRequest, long amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount.");
            return false;
        }
        System.out.println("Sent " + amount + " satoshis to payment request: " + paymentRequest);
        return true;
    }

    /**
     * Simule la génération d'une demande de paiement Lightning.
     */
    public String createPaymentRequest(long amount) {
        return "lnbc" + amount + "RandomLightningRequest12345";
    }
}
