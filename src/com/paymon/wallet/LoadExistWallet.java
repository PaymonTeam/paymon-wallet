package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LoadExistWallet extends JFrame {
    private JPanel panel;
    private JPanel titlePanel;
    private JLabel title;
    private JPanel passFieldPanel;
    private JPasswordField passwordField;
    private JButton oButton;
    private boolean oButtonIsClicked = true;
    private JLabel passwordLable;

    private JPanel loadButtonPanel;
    private JButton loadButton;
    private JPanel fileExplorerPanel;
    private ImagePanel imgPanel;

    private Handler handler;

    public LoadExistWallet() {
        handler = new Handler();
        setContentPane(panel);
        SetBackgroundImage();
        panel.setLayout(new GridLayout(4,1,20, 20));
        setTitle("Wallet Load");


        titlePanel = new JPanel();
        titlePanel.setOpaque(true);
        title = new JLabel("Load Exist Wallet");
        titlePanel.add(title);
        panel.add(titlePanel);

        fileExplorerPanel = new JPanel();
        JFilePicker filePicker = new JFilePicker("Pick a file", "Browse...");
        filePicker.setMode(JFilePicker.MODE_OPEN);
        filePicker.addFileTypeFilter(".json", "JSON Files");
        fileExplorerPanel.add(filePicker);
        panel.add(fileExplorerPanel);


        passFieldPanel = new JPanel();
        passFieldPanel.setLayout(new GridLayout(2,1));
        passwordField = new JPasswordField();
        oButton = new JButton();

        passwordLable = new JLabel();

        JPanel lablePanel = new JPanel();
        lablePanel.add(passwordLable);

        JPanel passPanel = new JPanel();
        passPanel.setLayout(new GridLayout(1,2));
        passPanel.add(passwordField);
        passPanel.add(oButton);

        handler.oButtonHandler(oButtonIsClicked, oButton, passwordField);
        oButton.setMaximumSize(new Dimension(78,30));
        oButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oButtonIsClicked = !oButtonIsClicked;
                handler.oButtonHandler(oButtonIsClicked, oButton, passwordField);
            }
        });

        passwordLable.setText("Password");
        passFieldPanel.add(lablePanel);
        passFieldPanel.add(passPanel);
        panel.add(passFieldPanel);

        loadButtonPanel = new JPanel();
        loadButton = new JButton("Load");
        loadButtonPanel.add(loadButton);
        panel.add(loadButtonPanel);

        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        if(panel != null) {
            setSize(panel.getWidth(), panel.getHeight());
        }
*/
        setDefaultLookAndFeelDecorated(false);
        pack();
    }

    public void SetBackgroundImage() {
        //panel = new ImagePanel(new ImageIcon(this.getClass().getResource("/background.png")).getImage());
    }

}


