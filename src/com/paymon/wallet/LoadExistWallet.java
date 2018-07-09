package com.paymon.wallet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LoadExistWallet extends JFrame {
    private JPanel panel;
    private JPanel fileExplorerPanel;
    private JPanel messagePanel;
    private JPanel buttPanel;
    private JPanel messageFileExplorerPanel;
    private JPanel firstPanel;
    private JPanel passFieldPanel;

    private JButton oButton;
    public JButton loadButton;

    private JPasswordField password;

    private JLabel passwordLable;
    private JLabel incorrectPassMessage;
    private JLabel fileExplorerMessageLabel;
    private JLabel enterPasswordLabel;
    private JLabel loadWalletLabel;
    public JButton backButton;

    private JFilePicker filePicker;

    private Handler handler;

    private boolean oButtonIsClicked = true;

    public LoadExistWallet() {

        initComponents();
        visibleSetter();

        oButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oButtonIsClicked = !oButtonIsClicked;
                handler.oButtonHandler(oButtonIsClicked, oButton, password);
            }
        });

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaultLookAndFeelDecorated(false);
        //pack();
    }

    private void visibleSetter(){
        messagePanel.setVisible(true);
        messagePanel.setOpaque(false);

        messageFileExplorerPanel.setVisible(true);
        messageFileExplorerPanel.setOpaque(false);

        incorrectPassMessage.setVisible(true);

        fileExplorerMessageLabel.setVisible(true);

        fileExplorerPanel.setOpaque(false);

        firstPanel.setOpaque(false);

        passFieldPanel.setOpaque(false);

        buttPanel.setOpaque(false);

        fileExplorerPanel.setOpaque(false);

        //setVisible(true);
    }

    private void initComponents(){
        handler = new Handler();
        setContentPane(panel);
        setTitle("Wallet Load");
        setSize(480, 480);


        filePicker = new JFilePicker("Pick a file", "Browse...");
        filePicker.setMode(JFilePicker.MODE_OPEN);
        filePicker.addFileTypeFilter(".json", "JSON Files");
        filePicker.setOpaque(true);
        filePicker.setBackground(new Color(51,181,229));
        fileExplorerPanel.add(filePicker, BorderLayout.CENTER);

        incorrectPassMessage.setForeground(new Color(51,181,229));

        fileExplorerMessageLabel.setForeground(new Color(51,181,229));

        panel.setBackground(new Color(51,181,229));

        loadWalletLabel.setForeground(Color.WHITE);

        enterPasswordLabel.setForeground(Color.WHITE);

        handler.oButtonHandler(oButtonIsClicked, oButton, password);

    }

    public boolean loadButtonHandler(){
        boolean passIsOk;
        boolean fileExplorerIsOk;
        passIsOk = handler.passFieldHandler(password, incorrectPassMessage, new Color(51,181,229));

        if(handler.fileIsCorrect(filePicker.getSelectedFilePath(),"json")){
            File f = new File(filePicker.getSelectedFilePath());
            if(f.exists() && !f.isDirectory()) {
                System.out.println("Selected .json file: " + filePicker.getSelectedFilePath());
                fileExplorerMessageLabel.setForeground(new Color(51,181,229));
                fileExplorerIsOk =  true;
            }else{
                fileExplorerMessageLabel.setText("File in selected path does not exist");
                fileExplorerMessageLabel.setForeground(Color.RED);
                fileExplorerIsOk =  false;
            }
        }else{
            System.out.println("Wrong file selected");
            fileExplorerMessageLabel.setText("Wrong file type selected");
            fileExplorerMessageLabel.setForeground(Color.RED);
            fileExplorerIsOk =  false;
        }
        return passIsOk && fileExplorerIsOk;
    }
    public String getPassword(){
        return password.getPassword().toString();
    }
    public String getPath(){
        return filePicker.getSelectedFilePath();
    }

}


