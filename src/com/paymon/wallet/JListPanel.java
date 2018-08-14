package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JListPanel extends JPanel {

    private List<TransactionInfoInWalletForm> txList;
    private JList<TransactionInfoInWalletForm> txJList;

    public JListPanel(List<TransactionInfoInWalletForm> txList) {
        this.txList = txList;
    }

    private JList<TransactionInfoInWalletForm> createList() {

        DefaultListModel<TransactionInfoInWalletForm> model = new DefaultListModel<>();
        for (TransactionInfoInWalletForm tx : this.txList) {
            model.addElement(tx);
        }
        JList<TransactionInfoInWalletForm> list = new JList<TransactionInfoInWalletForm>(model);
        list.setCellRenderer(new TxRenderer());
        return list;
    }

    public JPanel createPanel(Color background) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel panel = new JPanel(new GridBagLayout());
        JScrollPane scroll = new JScrollPane(this.txJList = createList());
        scroll.getVerticalScrollBar().setBackground(background);
        scroll.setBackground(background);
        panel.add(scroll, gbc);
        return panel;
    }
}
