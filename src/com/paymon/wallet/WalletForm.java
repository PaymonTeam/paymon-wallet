package com.paymon.wallet;

import javax.swing.*;
import java.util.Vector;

public class WalletForm extends JFrame {


    public JToolBar toolbar;
    private JPanel panel;
    private JButton addressButton;
    public JList list1;
    public TransactionListModel transactionListModel;

    public WalletForm() {
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

    private void createUIComponents() {
        transactionListModel = new TransactionListModel();
        list1 = new JList(transactionListModel);
    }
}
