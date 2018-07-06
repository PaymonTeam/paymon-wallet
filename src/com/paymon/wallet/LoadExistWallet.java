package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LoadExistWallet extends JFrame {
    private JFrame frame = this;
    private JPanel panel;
    private JPanel passFieldPanel;
    private JPasswordField passwordField;
    private JButton oButton;
    private JPasswordField password;
    private JPanel firstPanel;
    private JLabel LoadWalletLabel;
    private JLabel enterPasswordLabel;
    private JPanel buttPanel;
    private boolean oButtonIsClicked = true;
    private JLabel passwordLable;;
    private JButton loadButton;
    private JPanel fileExplorerPanel;
    private JPanel messagePanel;
    private JLabel incorrectPassMessage;
    private JPanel mainPanel;
    private JPanel messageFileExplorerPanel;
    private JLabel fileExplorerMessageLabel;
    private ImagePanel imgPanel;

    private Handler handler;

    public LoadExistWallet() {
        handler = new Handler();
        setContentPane(panel);
        setTitle("Wallet Load");
        setBackground(new Color(51,181,229));
        JFilePicker filePicker = new JFilePicker("Pick a file", "Browse...");
        filePicker.setMode(JFilePicker.MODE_OPEN);
        filePicker.addFileTypeFilter(".json", "JSON Files");
        fileExplorerPanel.add(filePicker, BorderLayout.CENTER);


        incorrectPassMessage.setForeground(Color.RED);
        fileExplorerMessageLabel.setForeground(Color.RED);
        messagePanel.setVisible(false);
        fileExplorerMessageLabel.setVisible(false);
        handler.oButtonHandler(oButtonIsClicked, oButton, password);
        oButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oButtonIsClicked = !oButtonIsClicked;
                handler.oButtonHandler(oButtonIsClicked, oButton, password);
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.passFieldHandler(password, messagePanel, incorrectPassMessage, frame);
                if(handler.fileIsCorrect(filePicker.getSelectedFilePath(),"json")){
                    File f = new File(filePicker.getSelectedFilePath());
                    if(f.exists() && !f.isDirectory()) {
                        System.out.println("Selected .json file: " + filePicker.getSelectedFilePath());
                        fileExplorerMessageLabel.setVisible(false);
                        pack();
                    }else{
                        fileExplorerMessageLabel.setText("File in selected path does not exist");
                        fileExplorerMessageLabel.setVisible(true);
                        pack();
                    }
                }else{
                    System.out.println("Wrong file selected");
                    fileExplorerMessageLabel.setText("Wrong file type selected");
                    fileExplorerMessageLabel.setVisible(true);
                    pack();
                }
            }
        });

        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaultLookAndFeelDecorated(false);
        pack();
    }


}


