package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;

public class WalletForm extends JFrame {
    public JToolBar toolbar;
    private JPanel panel;
    public JButton addressButton;
    private JPanel toolbarPanel;
    public JButton balanceButton;
    private JPanel textPanel;
    private JPanel JLPanel;
    private JPanel newTxPanel;
    private JButton createNewTransactionButton;
    private JButton refreshTransactionListButton;
    private JPanel refreshPanel;
    private JPanel mainPanel;
    public ArrayList<TransactionInfoInWalletForm> list = new ArrayList<>();

    public WalletForm() {
        initComponents();
        refreshTransactionListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateJListPanel();
            }
        });
        createNewTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
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

    private void initJListPanel() {
        JListPanel JLPan = new JListPanel(list);
        JLPanel.removeAll();
        JLPanel.add(JLPan.createPanel(),BorderLayout.CENTER);
    }
    private void setList(ArrayList<TransactionInfoInWalletForm> list){
        this.list = list;
    }

    public void updateJListPanel(){
        JListPanel JLPan = new JListPanel(list);
        JLPanel.removeAll();
        JLPanel.repaint();
        JLPanel.revalidate();
        JLPanel.add(JLPan.createPanel(), BorderLayout.CENTER);
        JLPanel.repaint();
        JLPanel.revalidate();
        panel.repaint();
        panel.revalidate();

    }
    public void setAddress(String address){
        addressButton.setText(address);
    }
    public void setBalance(int balance){
        balanceButton.setText(Integer.toString(balance));
    }
    private void initComponents(){
        setTitle("PaymonCoin Wallet");
        initToolbar();
        initJListPanel();
        add(panel);
    }

    private void initToolbar(){
        toolbar.setFloatable(false);
        toolbar.addSeparator();
        JButton fileToolbarButton = new JButton("File");
        toolbar.add(fileToolbarButton);
        toolbar.addSeparator();
        JButton optionsToolbarButton = new JButton("Options");
        toolbar.add(optionsToolbarButton);
        toolbar.addSeparator();
    }
    public void addToList(String hash, String sender, String recipient, int amount){
        list.add(new TransactionInfoInWalletForm(hash,sender,recipient,amount, Calendar.getInstance().getTime()));
    }
}
