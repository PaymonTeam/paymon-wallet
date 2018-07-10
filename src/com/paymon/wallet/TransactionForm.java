package com.paymon.wallet;

import javax.swing.*;
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

    private JFormattedTextField amount;

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
    }
}
