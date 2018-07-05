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
    public JButton balanceButton;
    private JPanel textPanel;
    private JLabel text1;
    private JLabel text2;
    private JPanel JLPanel;
    private JButton refreshButton;
    private ArrayList<TransactionInfoInWalletForm> list = new ArrayList<>();
    int n = 1;
    public WalletForm() {
        initComponents();
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TransactionInfoInWalletForm tx1 = new TransactionInfoInWalletForm("hash1", "sender1", "recipient1", 10, Calendar.getInstance().getTime());
                ArrayList<TransactionInfoInWalletForm> list = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    list.add(tx1);
                }
                n++;
                updateJListPanel();
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
        JLPanel = JLPan.createPanel();
        panel.add(JLPanel, 2);
    }
    private void setList(ArrayList<TransactionInfoInWalletForm> list){
        this.list = list;
    }

    public void updateJListPanel(){
        JListPanel JLPan = new JListPanel(list);
        panel.remove(JLPanel);
        JLPanel.removeAll();
        JLPanel.repaint();
        JLPanel.revalidate();
        JLPanel = JLPan.createPanel();
        JLPanel.repaint();
        JLPanel.revalidate();
        panel.add(JLPanel, 2);
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
        panel.setLayout(new GridLayout(5,1,20,20));
        initToolbar();
        initTextPanel();
        initJListPanel();
        initRefreshButton();
        add(panel);
    }
    private void initTextPanel(){
        text1 = new JLabel("Your wallet address");
        text2 = new JLabel("Your balance");
        addressButton = new JButton("Address");
        balanceButton = new JButton("Balance");
        textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(2,2,10,10));
        textPanel.add(text1);
        textPanel.add(addressButton);
        textPanel.add(text2);
        textPanel.add(balanceButton);

        panel.add(textPanel,1);
    }
    private void initToolbar(){
        toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.addSeparator();
        JButton fileToolbarButton = new JButton("File");
        toolbar.add(fileToolbarButton);
        toolbar.addSeparator();
        JButton optionsToolbarButton = new JButton("Options");
        toolbar.add(optionsToolbarButton);
        toolbar.addSeparator();

        panel.add(toolbar,0);
    }
    private void initRefreshButton(){
        refreshButton = new JButton("Refresh");
        panel.add(refreshButton,3);
    }
    public void addToList(String hash, String sender, String recipient, int amount){
        list.add(new TransactionInfoInWalletForm(hash,sender,recipient,amount, Calendar.getInstance().getTime()));
    }
}
