package com.paymon.wallet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransactionInfoInWalletForm {
    private String hash;
    private int amount;
    private String recipientAddress;
    private String senderAddress;
    private Date date;
    public TransactionInfoInWalletForm(String hash, String senderAddress, String recipientAddress, int amount, Date date) {
        super();
        this.hash = hash;
        this.senderAddress = senderAddress;
        this.recipientAddress = recipientAddress;
        this.amount = amount;
        this.date = date;
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


}
