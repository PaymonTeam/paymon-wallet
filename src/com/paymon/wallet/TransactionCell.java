package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;

public class TransactionCell extends JPanel implements ListCellRenderer<TransactionElement> {
    private static final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

    public JLabel value;

    public TransactionCell() {
        setOpaque(true);
        setVisible(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends TransactionElement> list, TransactionElement value, int index, boolean isSelected, boolean cellHasFocus) {
        this.value.setText(String.valueOf(value.getValue()));

        if (isSelected) {
            setBackground(HIGHLIGHT_COLOR);
            setForeground(Color.WHITE);
        } else {
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
        }
        return this;
    }
}
