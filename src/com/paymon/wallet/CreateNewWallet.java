package com.paymon.wallet;

import javax.swing.*;

public class CreateNewWallet extends JFrame {
    private JButton oButton;
    private JPanel panel;
    public JButton createNewWalletButton;
    private JPasswordField passwordField1;

    public CreateNewWallet() {
        setContentPane(panel);
        setVisible(true);
        setTitle("PaymonCoin GUI");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaultLookAndFeelDecorated(false);
        pack();
    }
}
