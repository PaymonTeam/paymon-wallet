package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

public class TransactionInfoForm extends JFrame{
    private JPanel panel;
    private JPanel mainLabelPanel;
    private JPanel addressesPanel;
    private JPanel infoPanel;
    private JPanel hashPanel;

    private JLabel transactionInfoLabel;
    private JLabel senderLabel;
    private JLabel amountLabel;
    private JLabel dateLabel;
    private JLabel arrowLabel;
    private JLabel amount;
    private JLabel hashLabel;
    private JLabel recipientLabel;

    public JButton xButton;
    private JButton hashButton;
    private JButton senderButton;
    private JButton recipientButton;

    private TransactionInfoInWalletForm tx;

    TransactionInfoForm(TransactionInfoInWalletForm tx) {
        this.tx = tx;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaultLookAndFeelDecorated(false);

        setContentPane(panel);
        setSize(450, 650);
        setFonts();
        setInfo();
        hashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection ss = new StringSelection(hashButton.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
            }

        });
        senderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection ss = new StringSelection(senderButton.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
            }
        });
        recipientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection ss = new StringSelection(recipientButton.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
            }
        });

        hashButton.setOpaque(false);
        hashButton.setBorderPainted(false);
        hashButton.setOpaque(true);


    }
    private void setFonts(){

        InputStream isArkhip = CreateNewWallet.class.getResourceAsStream("/fonts/Arkhip_font.ttf");
        InputStream isRoboto = CreateNewWallet.class.getResourceAsStream("/fonts/Roboto-Thin.ttf");
        try {
            Font arkhip = Font.createFont(Font.TRUETYPE_FONT, isArkhip);
            Font roboto = Font.createFont(Font.TRUETYPE_FONT, isRoboto);
            arkhip = arkhip.deriveFont(28f);
            roboto = roboto.deriveFont(20f);
            amountLabel.setFont(roboto);
            arrowLabel.setFont(roboto);
            dateLabel.setFont(roboto);
            hashLabel.setFont(roboto);
            senderLabel.setFont(roboto);
            amount.setFont(roboto);
            recipientLabel.setFont(roboto);
            transactionInfoLabel.setFont(arkhip);

            hashButton.setFont(roboto);
            xButton.setFont(roboto);
            senderButton.setFont(roboto);
            recipientButton.setFont(roboto);

        }catch (Exception ex){
            System.out.println("Incorrect font");
        }
    }
    private void setInfo(){
        hashButton.setText(tx.getHash());
        recipientButton.setText(tx.getRecipientAddress());
        senderButton.setText(tx.getSenderAddress());
        amount.setText(Integer.toString(tx.getAmount()));
        dateLabel.setText(tx.getDate().toString());
    }
}
