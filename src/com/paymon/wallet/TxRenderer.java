package com.paymon.wallet;


import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

class TxRenderer extends JPanel implements ListCellRenderer<TransactionInfoInWalletForm> {

    private JButton lbHash = new JButton();
    private JLabel lbAmount = new JLabel();
    private JLabel lbRecipient = new JLabel();
    public TxRenderer() {
        setLayout(new GridBagLayout());

        lbHash.setForeground(new Color(0xC2C2C2));
        lbAmount.setForeground(new Color(0xC2C2C2));
        lbRecipient.setForeground(new Color(0xC2C2C2));
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.ipadx = 0;
        c.fill = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        c.insets.left = 25;
        add(lbHash, c);
        c.fill = GridBagConstraints.CENTER;
        c.insets.left = 0;
        c.gridx = 1;
        add(lbAmount, c);
        c.fill = GridBagConstraints.EAST;
        c.insets.right = 25;
        c.gridx = 2;
        add(lbRecipient, c);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends TransactionInfoInWalletForm> list,
                                                  TransactionInfoInWalletForm tx, int index, boolean isSelected, boolean cellHasFocus) {
        int amount = tx.getAmount();
        lbAmount.setText(Integer.toString(amount));
        lbHash.setText(tx.getHash().substring(0, 9) + "...");
        lbHash.setBorderPainted(false);
        lbHash.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection ss = new StringSelection(tx.getHash());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
            }
        });

        lbRecipient.setText(tx.getRecipientAddress().substring(0, 9) + "...");

        InputStream isRoboto = CreateNewWallet.class.getResourceAsStream("/fonts/Roboto-Thin.ttf");
        try {
            Font roboto = Font.createFont(Font.TRUETYPE_FONT, isRoboto);
            roboto = roboto.deriveFont(20f);
            lbHash.setFont(roboto);
            lbAmount.setFont(roboto);
            lbRecipient.setFont(roboto);
        }catch (Exception ex){
            System.out.println("Incorrect font");
        }


        lbHash.setOpaque(true);
        lbAmount.setOpaque(true);
        lbRecipient.setOpaque(true);

        list.setForeground(new Color(0xC2C2C2));
        list.setBackground(new Color(0x323232));
        list.setSelectionBackground(new Color(0xC2C2C2));
        list.setSelectionForeground(new Color(0x323232));

        if (isSelected) {
            lbAmount.setBackground(list.getSelectionBackground());
            lbAmount.setForeground(list.getBackground());
            lbHash.setBackground(list.getSelectionBackground());
            lbHash.setForeground(list.getBackground());
            lbRecipient.setBackground(list.getSelectionBackground());
            lbRecipient.setForeground(list.getBackground());
            setBackground(list.getSelectionBackground());

        } else {
            lbAmount.setBackground(list.getBackground());
            lbAmount.setForeground(list.getForeground());
            lbHash.setBackground(list.getBackground());
            lbHash.setForeground(list.getForeground());
            lbRecipient.setBackground(list.getBackground());
            lbRecipient.setForeground(list.getForeground());
            setBackground(list.getBackground());
        }
        return this;
    }
}
