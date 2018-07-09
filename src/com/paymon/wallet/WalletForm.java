package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;

public class WalletForm extends JFrame {
    public JToolBar toolbar;

    public JPanel panel;
    private JPanel toolbarPanel;
    private JPanel textPanel;
    private JPanel JLPanel;
    private JPanel newTxPanel;
    private JPanel refreshPanel;
    private JPanel mainPanel;

    public JButton balanceButton;
    public JButton addressButton;
    public JButton createNewTransactionButton;
    public JButton refreshTransactionListButton;

    private JLabel addrLabel;
    private JLabel balanceLabel;

    public ArrayList<TransactionInfoInWalletForm> list = new ArrayList<>();

    public WalletForm() {
        initComponents();
        visibleSetter();

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

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaultLookAndFeelDecorated(false);
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

    //TODO change by using public access
    public void setAddress(String address){
        addressButton.setText(address);
    }

    public void setBalance(int balance){
        balanceButton.setText(Integer.toString(balance));
    }

    private void initComponents(){
        setTitle("PaymonCoin Wallet");
        setSize(480, 480);
        setContentPane(panel);
        initToolbar();
        initJListPanel();
    }
    public void visibleSetter(){
        JLPanel.setOpaque(false);

        toolbarPanel.setOpaque(false);

        refreshPanel.setOpaque(false);

        newTxPanel.setOpaque(false);

        textPanel.setOpaque(false);

        addrLabel.setForeground(Color.WHITE);

        balanceLabel.setForeground(Color.WHITE);

        panel.setBackground(new Color(51, 181, 229));

        mainPanel.setBackground(new Color(51, 181, 229));

        //setVisible(true);
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
    public void repaintMainPanel(){
        getContentPane().repaint();
        getContentPane().revalidate();
    }
}
