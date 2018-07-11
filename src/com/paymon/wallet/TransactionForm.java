package com.paymon.wallet;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class TransactionForm extends JFrame {
    private JPanel panel;
    private JPanel textPanel;
    private JPanel infoPanel;
    private JPanel sendPanel;
    private JPanel navigationPanel;


    private JButton addressButton;
    private JButton balanceButton;
    public JButton sendButton;
    public JButton backToWalletPageButton;

    private JTextField recipientAddress;

    private JTextField amount;

    private JLabel newTransactionLabel;
    private JLabel addrLabel;
    private JLabel balanceLabel;
    private JLabel recipientLabel;
    private JLabel amoutLabel;
    private JLabel exceptionLabel;
    private JLabel addressException;
    private JLabel amountException;

    TransactionForm() {
        initComponents();
        visibleSetter();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaultLookAndFeelDecorated(false);
    }

    private void visibleSetter() {
        textPanel.setOpaque(false);

        infoPanel.setOpaque(false);

        sendPanel.setOpaque(false);

        navigationPanel.setOpaque(false);

        newTransactionLabel.setForeground(Color.WHITE);

        addrLabel.setForeground(Color.WHITE);

        amoutLabel.setForeground(Color.WHITE);

        recipientLabel.setForeground(Color.WHITE);

        balanceLabel.setForeground(Color.WHITE);

        exceptionLabel.setVisible(true);

        exceptionLabel.setForeground(new Color(51, 181, 229));

        addressException.setVisible(true);

        addressException.setForeground(new Color(51, 181, 229));

        amountException.setVisible(true);

        amountException.setForeground(new Color(51, 181, 229));
    }

    private void initComponents() {

        setTitle("New Transaction");
        //setSize(480, 480);
        setContentPane(panel);
        PlainDocument doc = (PlainDocument) amount.getDocument();
        doc.setDocumentFilter(new DigitFilter());
    }

    public void setAddress(String address) {
        addressButton.setText(address);
    }

    public void setBalance(int balance) {
        balanceButton.setText(Integer.toString(balance));
    }

    public boolean txHandler() {
        boolean amountIsOk;
        boolean addressIsOk;

        if (amount.getText().equals("")) {
            amountIsOk = false;
            showAmountExceptionMessage(true, "Amount field must not be empty");
        } else {
            try {
                if (Integer.parseInt(amount.getText()) <= 0) {
                    amountIsOk = false;
                    showAmountExceptionMessage(true, "The amount must be greater than zero");
                } else {
                    amountIsOk = true;
                    showAmountExceptionMessage(false, "");
                }
            } catch (NumberFormatException e) {
                amountIsOk = true;
                showAmountExceptionMessage(false, "");
            }
        }
        if (recipientAddress.getText().equals("")) {
            addressIsOk = false;
            showAddressExceptionMessage(true, "Address field must not be empty");
        } else {
            try {
                Address addr = new Address(recipientAddress.getText());
                addressIsOk = addr.verify();
            } catch (Exception e) {
                addressIsOk = false;
            }
            showAddressExceptionMessage(!addressIsOk, addressIsOk ? "" : "Incorrect address");
        }
        return amountIsOk && addressIsOk;
    }

    public String getRecipientAddress() {
        return recipientAddress.getText();
    }

    public int getAmount() {
        if (amount.getText() != null) {
            return Integer.parseInt(amount.getText());
        } else {
            return -1;
        }
    }

    public void showExceptionMessage(boolean flag, String message) {
        if (message != null) {
            exceptionLabel.setText(message);
        }
        if (flag) {
            exceptionLabel.setForeground(Color.RED);
        } else {
            exceptionLabel.setForeground(panel.getForeground());
        }
    }

    private void showAmountExceptionMessage(boolean flag, String message) {
        if (message != null) {
            amountException.setText(message);
        }
        if (flag) {
            amountException.setForeground(Color.RED);
        } else {
            amountException.setForeground(panel.getForeground());
        }
    }

    private void showAddressExceptionMessage(boolean flag, String message) {
        if (message != null) {
            addressException.setText(message);
        }
        if (flag) {
            addressException.setForeground(Color.RED);
        } else {
            addressException.setForeground(panel.getForeground());
        }
    }

}

class DigitFilter extends DocumentFilter {
    private static final String DIGITS = "\\d+";

    @Override

    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {

        if (string.matches(DIGITS)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
        if (string.matches(DIGITS)) {
            super.replace(fb, offset, length, string, attrs);
        }
    }
}
