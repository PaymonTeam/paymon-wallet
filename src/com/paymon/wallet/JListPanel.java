package com.paymon.wallet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class JListPanel extends JPanel {

    private List<TransactionInfoInWalletForm> txList;
    private JList<TransactionInfoInWalletForm> txJList;

    public JListPanel(List<TransactionInfoInWalletForm> txList){
        this.txList = txList;
    }
    private JList<TransactionInfoInWalletForm> createList() {
        // create List model
        DefaultListModel<TransactionInfoInWalletForm> model = new DefaultListModel<>();
        // add item to model
        for (TransactionInfoInWalletForm tx: this.txList) {
            model.addElement(tx);
        }
        // create JList with model
        JList<TransactionInfoInWalletForm> list = new JList<TransactionInfoInWalletForm>(model);
        // set cell renderer
        list.setCellRenderer(new TxRenderer());
        return list;
    }
    public JPanel createPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        // create list book and set to scrollpane and add to panel
        panel.add(new JScrollPane(this.txJList = createList()),
                BorderLayout.CENTER);
        return panel;
    }
}
