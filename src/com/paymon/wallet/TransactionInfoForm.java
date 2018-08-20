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
public class TransactionInfoForm extends JFrame{
    private JPanel panel;
    private JPanel mainLabelPanel;
    private JLabel transactionInfoLabel;
    private JPanel infoPanel;
    public JButton xButton;
    private JPanel addressesPanel;
    private JLabel senderLabel;
    private JLabel amountLabel;
    private JPanel hashPanel;
    private JLabel hashLabel;
    private JTextArea textArea1;
    private JButton copyButton;
    private JLabel recipientLabel;

    TransactionInfoForm(TransactionInfoInWalletForm tx) {
        setContentPane(panel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(450, 650);

    }
}
