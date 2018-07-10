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
