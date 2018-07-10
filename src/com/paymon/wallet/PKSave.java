package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class PKSave extends JFrame {
    private JPanel panel;
    private JPanel titlePanel;
    private JPanel PKPanel;
    private JPanel navigationPanel;
    public JButton backButton;
    public JButton finishButton;
    public JTextField privateKeyTextField;
    public JButton copyButton;
    private JLabel titleLabel;

    public PKSave() {
        initComponents();
        visibleSetter();
    }

    private void initComponents() {
        setContentPane(panel);
        setSize(480, 480);
    }

    private void visibleSetter() {
        titlePanel.setOpaque(false);
        PKPanel.setOpaque(false);

//        navigationPanel.setOpaque(false);

        titleLabel.setForeground(Color.WHITE);

        panel.setBackground(new Color(51, 181, 229));
    }

    public void setClipboard(String str) {
        StringSelection ss = new StringSelection(str);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
    }
}
