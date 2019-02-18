package org.paymon.wallet;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

public class TransactionForm extends JFrame {
    private JPanel panel;
    private JPanel textPanel;
    private JPanel infoPanel;
    private JPanel sendPanel;
    private JPanel navigationPanel;


    public JButton addressButton;
    public JButton balanceButton;
    public JButton sendButton;
    public JButton backToWalletPageButton;

    private JTextField recipientAddress;

    private JTextField amount;

    private JLabel newTransactionLabel;
    private JLabel addrLabel;
    private JLabel balanceLabel;
    private JLabel recipientLabel;
    private JLabel amoutLabel;
    private JLabel exceptionLabel;
    private JLabel addressException;
    private JLabel amountException;
    private JLabel hintLabel;

    private int backgroundColor = 0x323232;
    private int red = 0xe15754;
    private int labelColor = 0xc2c2c2;

    TransactionForm() {
        initComponents();
        visibleSetter();
        setResizable(false);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaultLookAndFeelDecorated(false);

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

    private void visibleSetter() {
        textPanel.setOpaque(false);

        infoPanel.setOpaque(false);

        sendPanel.setOpaque(false);

        navigationPanel.setOpaque(false);

        newTransactionLabel.setForeground(new Color(labelColor));

        addrLabel.setForeground(new Color(labelColor));

        amoutLabel.setForeground(new Color(labelColor));

        recipientLabel.setForeground(new Color(labelColor));

        balanceLabel.setForeground(new Color(labelColor));

        exceptionLabel.setVisible(true);

        exceptionLabel.setForeground(new Color(backgroundColor));

        addressException.setVisible(true);

        addressException.setForeground(new Color(backgroundColor));

        amountException.setVisible(true);

        amountException.setForeground(new Color(backgroundColor));

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
    }

    private void setFonts() {
        InputStream isArkhip = CreateNewWallet.class.getResourceAsStream("/fonts/Arkhip_font.ttf");
        InputStream isRoboto = CreateNewWallet.class.getResourceAsStream("/fonts/Roboto-Thin.ttf");
        try {
            Font arkhip = Font.createFont(Font.TRUETYPE_FONT, isArkhip);
            Font roboto = Font.createFont(Font.TRUETYPE_FONT, isRoboto);
            arkhip = arkhip.deriveFont(28f);
            roboto = roboto.deriveFont(20f);
            newTransactionLabel.setFont(arkhip);
            addrLabel.setFont(roboto);
            balanceLabel.setFont(roboto);
            exceptionLabel.setFont(roboto);
            addressButton.setFont(roboto);
            balanceButton.setFont(roboto);
            amoutLabel.setFont(roboto);
            recipientLabel.setFont(roboto);
            exceptionLabel.setFont(roboto);
            addressException.setFont(roboto);
            amountException.setFont(roboto);
            recipientAddress.setFont(roboto);
            amount.setFont(roboto);
            sendButton.setFont(roboto);
            backToWalletPageButton.setFont(roboto);
            roboto = roboto.deriveFont(15f);
            hintLabel.setFont(roboto);
        } catch (Exception ex) {
            System.out.println("Incorrect font");
        }
    }

    private void initComponents() {

        setTitle("New Transaction");
        setSize(450, 650);
        setContentPane(panel);
        setFonts();
        PlainDocument doc = (PlainDocument) amount.getDocument();
        doc.setDocumentFilter(new DigitFilter());
    }

    public void setAddress(String address) {
        addressButton.setText(address);
    }

    public void setBalance(int balance) {
        balanceButton.setText(Integer.toString(balance));
    }

    public boolean txHandler(int balance) {
        boolean amountIsOk;
        boolean addressIsOk;

        if (amount.getText().equals("")) {
            amountIsOk = false;
            showAmountExceptionMessage(true, "Amount field must not be empty");
        } else {
            try {
                if (Integer.parseInt(amount.getText()) < 0) {
                    amountIsOk = false;
                    showAmountExceptionMessage(true, "The amount must be greater than zero");
                } else {
                    if (Integer.parseInt(amount.getText()) > balance) {
                        showAmountExceptionMessage(true, "Insufficient balance");
                        amountIsOk = false;
                    } else {
                        amountIsOk = true;
                        showAmountExceptionMessage(false, " ");
                    }
                }
            } catch (NumberFormatException e) {
                amountIsOk = true;
                showAmountExceptionMessage(false, " ");
            }
        }
        if (recipientAddress.getText().equals(" ")) {
            addressIsOk = false;
            showAddressExceptionMessage(true, "Address field must not be empty");
        } else {
            try {
                Address addr = new Address(recipientAddress.getText());
                addressIsOk = addr.verify();
            } catch (Exception e) {
                addressIsOk = false;
            }
            showAddressExceptionMessage(!addressIsOk, addressIsOk ? " " : "Incorrect address");
        }
        return amountIsOk && addressIsOk;
    }

    public String getRecipientAddress() {
        return recipientAddress.getText();
    }

    public int getAmount() {
        if (amount.getText() != null) {
            return Integer.parseInt(amount.getText());
        } else {
            return -1;
        }
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

    private void showAmountExceptionMessage(boolean flag, String message) {
        if (message != null) {
            amountException.setText(message);
        }
        if (flag) {
            amountException.setForeground(new Color(red));
        } else {
            amountException.setForeground(new Color(backgroundColor));
        }
    }

    private void showAddressExceptionMessage(boolean flag, String message) {
        if (message != null) {
            addressException.setText(message);
        }
        if (flag) {
            addressException.setForeground(new Color(red));
        } else {
            addressException.setForeground(new Color(backgroundColor));
        }
    }

    public void showExceptionMessage(Color color, String message) {
        if (message != null) {
            exceptionLabel.setText(message);
        }
        if (color != null) {
            exceptionLabel.setForeground(color);
        }
    }

    public void clear() {
        addressException.setForeground(new Color(backgroundColor));
        amountException.setForeground(new Color(backgroundColor));
        exceptionLabel.setForeground(new Color(backgroundColor));
        amount.setText("0");
        recipientAddress.setText("");
    }

}

