package com.paymon.wallet;

import java.awt.event.MouseAdapter;
import java.util.Comparator;
import java.util.Date;

public class TransactionInfoInWalletForm {
    private String hash;
    private int amount;
    private String recipientAddress;
    private String senderAddress;
    private Date date;
    private boolean isConfirmed;
    private MouseAdapter panel;

    public TransactionInfoInWalletForm(String hash, String senderAddress, String recipientAddress, int amount, Date date, MouseAdapter panel) {
        super();
        this.hash = hash;
        this.senderAddress = senderAddress;
        this.recipientAddress = recipientAddress;
        this.amount = amount;
        this.date = date;
        this.panel = panel;
    }

    public TransactionInfoInWalletForm(String hash, String senderAddress, String recipientAddress, int amount, Date date) {
        super();
        this.hash = hash;
        this.senderAddress = senderAddress;
        this.recipientAddress = recipientAddress;
        this.amount = amount;
        this.date = date;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public String getHash() {
        return hash;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public int getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public MouseAdapter getPanel() {
        return panel;
    }

    public static final Comparator<TransactionInfoInWalletForm> COMPARE_BY_DATE = new Comparator<TransactionInfoInWalletForm>() {
        @Override
        public int compare(TransactionInfoInWalletForm lhs, TransactionInfoInWalletForm rhs) {
            return lhs.getDate().compareTo(rhs.getDate());
        }
    };
}

