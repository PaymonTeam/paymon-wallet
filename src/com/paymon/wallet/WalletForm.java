package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

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
    private JLabel exceptionLabel;

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
        addressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection ss = new StringSelection(addressButton.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
            }

        });
        balanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection ss = new StringSelection(balanceButton.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
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
        JLPanel.add(JLPan.createPanel(), BorderLayout.CENTER);
    }

    private void setList(ArrayList<TransactionInfoInWalletForm> list) {
        this.list = list;
    }

    public void updateJListPanel() {
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
    public void setAddress(String address) {
        addressButton.setText(address);
    }

    public void setBalance(int balance) {
        balanceButton.setText(Integer.toString(balance));
    }

    private void initComponents() {
        setTitle("PaymonCoin Wallet");
        setSize(480, 480);
        setContentPane(panel);
        initToolbar();
        initJListPanel();
        JLPanel.setSize(getSize().width - 20, getSize().height -
                (toolbarPanel.getHeight() + textPanel.getHeight() + newTxPanel.getHeight() + refreshPanel.getHeight() + exceptionLabel.getHeight()));
        repaintMainPanel();
    }

    public void visibleSetter() {
        JLPanel.setOpaque(false);

        toolbarPanel.setOpaque(false);

        refreshPanel.setOpaque(false);

        newTxPanel.setOpaque(false);

        textPanel.setOpaque(false);

        addrLabel.setForeground(Color.WHITE);

        balanceLabel.setForeground(Color.WHITE);

        panel.setBackground(new Color(51, 181, 229));

        mainPanel.setBackground(new Color(51, 181, 229));

        exceptionLabel.setVisible(true);
        exceptionLabel.setForeground(new Color(51, 181, 229));

        //setVisible(true);
    }

    private void initToolbar() {
        toolbar.setFloatable(false);
        toolbar.addSeparator();
        JButton fileToolbarButton = new JButton("File");
        toolbar.add(fileToolbarButton);
        toolbar.addSeparator();
        JButton optionsToolbarButton = new JButton("Options");
        toolbar.add(optionsToolbarButton);
        toolbar.addSeparator();
    }

    public void addToList(String hash, String sender, String recipient, int amount, long timestamp) {
        list.add(new TransactionInfoInWalletForm(hash, sender, recipient, amount, new Date(timestamp * 1000)));
    }

    public void repaintMainPanel() {
        getContentPane().repaint();
        getContentPane().revalidate();
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
}
