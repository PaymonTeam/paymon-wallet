package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.List;

public class JListPanel extends JPanel {

    private List<TransactionInfoInWalletForm> txList;
    private JList<TransactionInfoInWalletForm> txJList;
    private MouseListener mouseListener;

    public JListPanel(List<TransactionInfoInWalletForm> txList, MouseListener mouseListener) {
        this.txList = txList;
        this.mouseListener = mouseListener;
    }

    public JListPanel(List<TransactionInfoInWalletForm> txList) {
        this.txList = txList;
    }

    private JList<TransactionInfoInWalletForm> createList() {

        DefaultListModel<TransactionInfoInWalletForm> model = new DefaultListModel<>();
        for (TransactionInfoInWalletForm tx : this.txList) {
            model.addElement(tx);
        }
        JList<TransactionInfoInWalletForm> list = new JList<TransactionInfoInWalletForm>(model);
        list.addMouseListener(this.mouseListener);
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
        txJList = createList();
        txJList.setOpaque(false);
        txJList.setBackground(background);
        txJList.setOpaque(true);
        JScrollPane scroll = new JScrollPane(txJList);
        scroll.setOpaque(false);
        scroll.getVerticalScrollBar().setBackground(background);
        scroll.setBackground(background);
        scroll.setOpaque(true);
        panel.add(scroll, gbc);
        return panel;
    }
}
