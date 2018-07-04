package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;

public class WalletForm extends JFrame {
    public JToolBar toolbar;
    private JPanel panel;
    public JButton addressButton;
    private JPanel textPanel;
    private JLabel text;
    private JPanel JLPanel;

    public WalletForm() {
        setTitle("PaymonCoin Wallet");

        text = new JLabel("Your wallet address");
        textPanel = new JPanel();
        addressButton = new JButton("Address");
        toolbar = new JToolBar();
        add(panel);

        panel.setLayout(new BorderLayout());
        panel.add(toolbar, BorderLayout.PAGE_START);
        textPanel.setLayout(new BorderLayout());
        textPanel.add(text, BorderLayout.WEST);
        textPanel.add(addressButton, BorderLayout.EAST);
        panel.add(textPanel, BorderLayout.CENTER);

        toolbar.setFloatable(false);
        toolbar.addSeparator();
        JButton fileToolbarButton = new JButton("File");
        toolbar.add(fileToolbarButton);
        toolbar.addSeparator();
        JButton optionsToolbarButton = new JButton("Options");
        toolbar.add(optionsToolbarButton);
        toolbar.addSeparator();

        createTxJList();

        setVisible(true);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaultLookAndFeelDecorated(false);
        pack();
    }

    private void createTxJList() {
        TransactionInfoInWalletForm tx1 = new TransactionInfoInWalletForm("hash1", "sender1", "recipient1", 10, Calendar.getInstance().getTime());
        TransactionInfoInWalletForm tx2 = new TransactionInfoInWalletForm("hash2", "sender2", "recipient2", -10, Calendar.getInstance().getTime());

        ArrayList<TransactionInfoInWalletForm> list = new ArrayList<>();
        list.add(tx1);
        list.add(tx2);
        JListPanel JLPan = new JListPanel(list);
        JLPanel = JLPan.createPanel();
        panel.add(JLPanel, BorderLayout.SOUTH);
    }
}
