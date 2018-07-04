package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LoadExistWallet extends JFrame {
    private JPanel panel;
    private JButton loadButton;
    private JPanel formPanel;
    private JPanel titlePanel;
    private JPanel loadButtonPanel;
    private JPanel passFieldPanel;
    private JButton oButton;
    private JPasswordField password;
    private JPanel fileExplorerPanel;
    private ImagePanel imgPanel;

    public LoadExistWallet() {
        SetBackgroundImage();
        panel.setLayout(new GridBagLayout());
        setTitle("Wallet Load");
        GridBagConstraints c =  new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill   = GridBagConstraints.NONE;
        c.gridheight = 1;
        c.gridwidth  = GridBagConstraints.REMAINDER;
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 0, 0, 0);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        panel.add(titlePanel, c);
        c.weightx = 1.0;
        fileExplorerPanel = new JPanel();
        panel.add(fileExplorerPanel, c);
        c.weightx = 2.0;
        panel.add(loadButtonPanel,c);
        add(panel);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(panel != null) {
            setSize(panel.getWidth(), panel.getHeight());
        }
        JFilePicker filePicker = new JFilePicker("Pick a file", "Browse...");
        filePicker.setMode(JFilePicker.MODE_OPEN);
        filePicker.addFileTypeFilter(".json", "JSON Files");
        panel.add(filePicker);
        setDefaultLookAndFeelDecorated(false);
        pack();
    }

    public void SetBackgroundImage() {
        panel = new ImagePanel(new ImageIcon(this.getClass().getResource("/background.png")).getImage());
    }

}


