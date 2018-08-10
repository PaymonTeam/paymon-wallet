package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.desktop.SystemEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class WalletForm extends JFrame {

    public JPanel panel;
    private JPanel toolbarPanel;
    private JPanel textPanel;
    private JPanel JLPanel;
    private JPanel newTxPanel;
    private JPanel refreshPanel;
    private JPanel mainPanel;
    private JPanel processPanel;

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



    private int backgroundColor = 0x323232;
    private int red = 0xe15754;
    private int labelColor = 0xc2c2c2;

    public ArrayList<TransactionInfoInWalletForm> list = new ArrayList<>();

    public WalletForm() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaultLookAndFeelDecorated(false);

        initComponents();
        visibleSetter();
        setResizable(false);

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

    }
    private void initGlassPanel(){
        processPanel = new JPanel(new GridBagLayout()){
            public void paintComponent(Graphics g)
            {
                g.setColor(new Color(50,50,50,225));
                g.fillRect(0,0, getWidth(), getHeight());
            }
        };

        processPanel.setOpaque(false);

        processLabel = new JLabel("Please wait, it may take a few minutes");
        processLabel.setForeground(new Color(labelColor));
        processLabel.setBackground(new Color(0x4A4A4A));

        processPanel.add(processLabel);
        processPanel.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent me){
                me.consume();
            }
        });

        setGlassPane(processPanel);

    }
    private void initJListPanel() {
        JListPanel JLPan = new JListPanel(list);
        JLPan.setOpaque(false);
        JPanel jList = JLPan.createPanel(new Color(backgroundColor));
        jList.setOpaque(false);
        JLPanel.removeAll();
        JLPanel.add(jList, BorderLayout.CENTER);
    }


    public void updateJListPanel() {
        JListPanel JLPan = new JListPanel(list);
        JLPanel.removeAll();
        JLPanel.repaint();
        JLPanel.revalidate();
        JPanel jlist = JLPan.createPanel(new Color(backgroundColor));
        JLPanel.add(jlist, BorderLayout.CENTER);
        JLPanel.repaint();
        JLPanel.revalidate();
        panel.repaint();
        panel.revalidate();

    }

    public void setAddress(String address) {
        addressButton.setText(address);
    }

    public void setBalance(int balance) {
        balanceButton.setText(Integer.toString(balance));
    }

    private void initComponents() {
        setTitle("PaymonCoin Wallet");
        setSize(450, 650);
        initGlassPanel();
        setFonts();
        setContentPane(panel);
        initJListPanel();
        repaintMainPanel();

    }

    public void visibleSetter() {
        JLPanel.setOpaque(false);

        toolbarPanel.setOpaque(false);

        refreshPanel.setOpaque(false);

        newTxPanel.setOpaque(false);

        textPanel.setOpaque(false);

        addrLabel.setForeground(new Color(labelColor));

        balanceLabel.setForeground(new Color(labelColor));

        panel.setBackground(new Color(backgroundColor));

        JLPanel.setBackground(new Color(backgroundColor));

        mainPanel.setBackground(new Color(backgroundColor));

        exceptionLabel.setVisible(true);
        exceptionLabel.setForeground(new Color(backgroundColor));

    }

    private void setFonts(){
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
            if(processLabel != null) {
                processLabel.setFont(roboto);
            }else{
                System.out.println("Nullable processLabel");
            }
            roboto = roboto.deriveFont(15f);
            hintLabel.setFont(roboto);
        }catch (Exception ex){
            System.out.println("Incorrect font");
        }
    }
    public void addToList(TransactionInfoInWalletForm tx) {
        list.add(tx);
    }
    public void setList(ArrayList<TransactionInfoInWalletForm> txList){
        list.clear();
        if(txList != null) {
            list = txList;
        }else{
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
    public void clear(){
        balanceButton.setText("");
        addressButton.setText("");
        JLPanel.removeAll();
    }
}
