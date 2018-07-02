package com.paymon.wallet;


import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

class TxRenderer extends JPanel implements ListCellRenderer<TransactionInfoInWalletForm> {

    private JLabel lbHash = new JLabel();
    private JLabel lbSender = new JLabel();
    private JLabel lbAmount = new JLabel();
    private JLabel lbRecipient = new JLabel();
    private JLabel lbDate = new JLabel();

    public TxRenderer() {
        setLayout(new BorderLayout(5, 5));

        JPanel panelText = new JPanel(new GridLayout(0, 1));
        panelText.add(lbHash);
        panelText.add(lbSender);
        panelText.add(lbRecipient);
        panelText.add(lbDate);
        add(lbAmount, BorderLayout.WEST);
        add(panelText, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends TransactionInfoInWalletForm> list,
                                                  TransactionInfoInWalletForm tx, int index, boolean isSelected, boolean cellHasFocus) {
        int amount = tx.getAmount();
        lbAmount.setText(Integer.toString(amount));
        if(amount > 0){
            lbAmount.setForeground(Color.GREEN);
        }
        if(amount < 0){
            lbAmount.setForeground(Color.RED);
        }
        if(amount == 0){
            lbAmount.setForeground(Color.BLUE);
        }
        lbHash.setText(tx.getHash());
        lbSender.setText(tx.getSenderAddress());
        lbRecipient.setText(tx.getRecipientAddress());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        lbDate.setText(df.format(tx.getDate()));
        lbDate.setForeground(Color.ORANGE);

        // set Opaque to change background color of JLabel
        lbHash.setOpaque(true);
        lbSender.setOpaque(true);
        lbAmount.setOpaque(true);
        lbRecipient.setOpaque(true);
        lbDate.setOpaque(true);

        // when select item
        if (isSelected) {
            lbAmount.setBackground(list.getSelectionBackground());
            lbDate.setBackground(list.getSelectionBackground());
            lbHash.setBackground(list.getSelectionBackground());
            lbRecipient.setBackground(list.getSelectionBackground());
            lbSender.setBackground(list.getSelectionBackground());
            setBackground(list.getSelectionBackground());

        } else { // when don't select
            lbAmount.setBackground(list.getBackground());
            lbDate.setBackground(list.getBackground());
            lbHash.setBackground(list.getBackground());
            lbRecipient.setBackground(list.getBackground());
            lbSender.setBackground(list.getBackground());
            setBackground(list.getBackground());
        }
        return this;
    }
}
