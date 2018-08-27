package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.InputStream;
import java.util.ArrayList;

public class WalletForm extends JFrame {
    public JPanel panel;
    private JPanel toolbarPanel;
    private JPanel textPanel;
    private JPanel JLPanel;
    private JPanel newTxPanel;
    private JPanel refreshPanel;
    private JPanel mainPanel;
    public JPanel processPanel;

    public JButton balanceButton;
    public JButton addressButton;
    public JButton createNewTransactionButton;
    public JButton refreshTransactionListButton;
    public JButton logoutToolbarButton;

    private JLabel addrLabel;
    private JLabel balanceLabel;
    private JLabel exceptionLabel;
    private JPanel formPanel;
    private JLabel id;
    private JLabel quantity;
    private JLabel address;
    private JLabel hintLabel;
    private JLabel processLabel;

    private MouseListener JLML;

    private int backgroundColor = 0x323232;
    private int red = 0xe15754;
    private int labelColor = 0xc2c2c2;

    public ArrayList<TransactionInfoInWalletForm> list = new ArrayList<>();

    public WalletForm() {
        initComponents();
        setResizable(false);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //SetBackgroundImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaultLookAndFeelDecorated(false);

        refreshTransactionListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JLML != null) {
                    updateJListPanel(JLML);
                }
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

    }

    private void initGlassPanel() {
        processPanel = new JPanel(new GridBagLayout()) {
            public void paintComponent(Graphics g) {
                g.setColor(new Color(50, 50, 50, 200));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        processPanel.setOpaque(false);

        processLabel = new JLabel("Please wait, it may take a few minutes");
        processLabel.setForeground(new Color(labelColor));
        processLabel.setBackground(new Color(0x4A4A4A));

        processPanel.add(processLabel);
        processPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                me.consume();
            }
        });

    }

    public void showTxInfo(TransactionInfoInWalletForm tx) {
        TransactionInfoForm txInfo = new TransactionInfoForm(tx);
        txInfo.xButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getGlassPane().setVisible(false);
            }
        });
        setGlassPane(txInfo.getContentPane());
    }

    public void initJListPanel() {
        JListPanel JLPan = new JListPanel(list);
        JLPan.setOpaque(false);
        JPanel jList = JLPan.createPanel(new Color(backgroundColor));
        jList.setOpaque(false);
        JLPanel.removeAll();
        JLPanel.add(jList, BorderLayout.CENTER);
    }

    public void updateJListPanel(MouseListener ml) {
        JLML = ml;
        JLPanel.removeAll();
        JLPanel.add(new JListPanel(list, ml).createPanel(new Color(backgroundColor)), BorderLayout.CENTER);
        JLPanel.repaint();
        JLPanel.revalidate();
    }

    public void setAddress(String address) {
        addressButton.setText(address);
    }

    public void setBalance(int balance) {
        balanceButton.setText(Integer.toString(balance));
    }

    private void initComponents() {
        setContentPane(panel);
        setTitle("PaymonCoin Wallet");
        setSize(450, 650);
        visibleSetter();
        initGlassPanel();
        setFonts();
        initJListPanel();
        repaintMainPanel();
    }

    public void visibleSetter() {
        JLPanel.setOpaque(true);
        formPanel.setOpaque(false);
        toolbarPanel.setOpaque(false);
        refreshPanel.setOpaque(false);
        newTxPanel.setOpaque(false);
        textPanel.setOpaque(false);

        addrLabel.setForeground(new Color(labelColor));
        balanceLabel.setForeground(new Color(labelColor));

        addressButton.setOpaque(false);
        addressButton.setBorderPainted(false);
        addressButton.setOpaque(true);
        addressButton.setBackground(new Color(194, 194, 194));
        addressButton.setForeground(new Color(94, 94, 94));

        balanceButton.setOpaque(false);
        balanceButton.setBorderPainted(false);
        balanceButton.setOpaque(true);
        balanceButton.setBackground(new Color(194, 194, 194));
        balanceButton.setForeground(new Color(94, 94, 94));

        panel.setBackground(new Color(backgroundColor));
        JLPanel.setBackground(new Color(backgroundColor));
        mainPanel.setBackground(new Color(backgroundColor));

        exceptionLabel.setVisible(true);
        exceptionLabel.setForeground(new Color(backgroundColor));
    }

    private void setFonts() {
        InputStream isRoboto = CreateNewWallet.class.getResourceAsStream("/fonts/Roboto-Thin.ttf");
        try {
            Font roboto = Font.createFont(Font.TRUETYPE_FONT, isRoboto);
            roboto = roboto.deriveFont(20f);
            addrLabel.setFont(roboto);
            balanceLabel.setFont(roboto);
            exceptionLabel.setFont(roboto);
            addressButton.setFont(roboto);
            balanceButton.setFont(roboto);
            logoutToolbarButton.setFont(roboto);
            exceptionLabel.setFont(roboto);
            refreshTransactionListButton.setFont(roboto);
            createNewTransactionButton.setFont(roboto);
            id.setFont(roboto);
            quantity.setFont(roboto);
            address.setFont(roboto);
            if (processLabel != null) {
                processLabel.setFont(roboto);
            } else {
                System.out.println("Nullable processLabel");
            }
            roboto = roboto.deriveFont(15f);
            hintLabel.setFont(roboto);
        } catch (Exception ex) {
            System.out.println("Incorrect font");
        }
    }

    public void setList(ArrayList<TransactionInfoInWalletForm> txList) {
        if (txList != null) {
            list = txList;
        } else {
            System.out.println("Nullable ArrayList object");
        }
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
            exceptionLabel.setForeground(new Color(red));
        } else {
            exceptionLabel.setForeground(new Color(backgroundColor));
        }
    }

    public void clear() {
        balanceButton.setText("");
        addressButton.setText("");
        JLPanel.removeAll();
    }
}
