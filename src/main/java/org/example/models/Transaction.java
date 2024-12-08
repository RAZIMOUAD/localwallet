package org.example.models;

import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private int senderWalletId;
    private String receiverWalletAddress;
    private double amount;
    private LocalDateTime createdAt;

    public Transaction(int id, int senderWalletId, String receiverWalletAddress, double amount, LocalDateTime createdAt) {
        this.id = id;
        this.senderWalletId = senderWalletId;
        this.receiverWalletAddress = receiverWalletAddress;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public int getSenderWalletId() {
        return senderWalletId;
    }

    public String getReceiverWalletAddress() {
        return receiverWalletAddress;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
