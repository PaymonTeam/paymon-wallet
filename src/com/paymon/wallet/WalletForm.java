package com.paymon.wallet;

import javax.swing.*;
import java.util.Vector;

public class WalletForm extends JFrame {


    public JToolBar toolbar;
    private JPanel panel;
    private JButton addressButton;
    public JList<TransactionElement> list1;
    public TransactionListModel transactionListModel;
    public DefaultListModel<TransactionElement> transactionListModel2;

    public WalletForm() {
        setContentPane(panel);
       // setVisible(true);
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
        transactionListModel2 = new DefaultListModel<>();
        transactionListModel2.addElement(new TransactionElement(1));
        transactionListModel2.addElement(new TransactionElement(2));

        list1 = new JList<>();
        list1.setModel(transactionListModel2);
        list1.setCellRenderer(new TransactionCell());
//        TransactionElement els[] = {
//                new TransactionElement(1),
//                new TransactionElement(2)
//        };
//        list1 = new JList<>(els);
    }
}
